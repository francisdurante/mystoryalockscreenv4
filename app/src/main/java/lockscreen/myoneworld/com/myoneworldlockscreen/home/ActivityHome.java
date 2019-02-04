package lockscreen.myoneworld.com.myoneworldlockscreen.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import lockscreen.myoneworld.com.myoneworldlockscreen.AppUpdateChecker;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.LockscreenService;

import android.support.v4.view.ViewPager;

import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;

public class ActivityHome extends AppCompatActivity {
    Context mContext = this;
    ImageView header;
    Button serviceButton;
    private ViewPager viewPager;
    NetworkChangeReceiver ncr;
    public static String updateStatus;
    public final static int REQUEST_CODE = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            if (!Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
            globalMessageBox(mContext,"Please enable Autostart on my|ONEworld lockscreen app","Autostart Application",MSG_BOX_WARNING);
        }

        init();
    }

    public void init() {
        try {
            switch (updateStatus) {
                case "outdated":
                    globalMessageBox(mContext, "New Version is now available in Google Play Store. Please update to continue using the lockscreen.", "Application Update", MSG_BOX_WARNING);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        iniDefaultSetting();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        header = findViewById(R.id.header);
        serviceButton = findViewById(R.id.stopService);
        ncr = new NetworkChangeReceiver(header, this);
        registerReceiver(ncr, intentFilter);
        serviceButton.setTypeface(setFont(mContext, "font/Century_Gothic.ttf"));

        if (isMyServiceRunning(LockscreenService.class, mContext)) {
            serviceButton.setText(STOP);

        } else {
            serviceButton.setText(START);
        }

        serviceButton.setOnClickListener(new View.OnClickListener() {
            Intent lockscreenService = new Intent(mContext, LockscreenService.class);

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("InvalidWakeLockTag")
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(LockscreenService.class, mContext)) {
                    serviceButton.setText(START);
                    stopService(lockscreenService);
                    stopJobService(mContext);
                    save("SERVICE", "0", mContext);
                } else {
                    serviceButton.setText(STOP);
                    createNotificationChannel(mContext);
                    ContextCompat.startForegroundService(mContext, lockscreenService);
                    save("SERVICE", "1", mContext);
                }
            }
        });

        viewPager = findViewById(R.id.main_activity_view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (isNetworkAvailable(mContext)) {
            save("connection", "true", mContext);
        }
    }

    @Override
    protected void onDestroy() {
        if (ncr != null)
            unregisterReceiver(ncr);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    showGPSDisabledAlertToUser();
                }
            }
        }
    }

    private void showGPSDisabledAlertToUser() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            HomeDAO.location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ignored) {}
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            HomeDAO.location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ignored) {}
        if(!gps_enabled && !network_enabled) {
            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(callGPSSettingIntent);
        }else{
            new HomeDAO(mContext,this).sendLocationTimer();
        }
    }
    private void iniDefaultSetting(){
        if(getValueString("WIFI_ONLY",mContext).equals("") && getValueString("WIFI_OR_DATA",mContext).equals("") && getValueString("DO_NOT_DOWNLOAD",mContext).equals("")){
            save("DO_NOT_DOWNLOAD","1",mContext);
        }
    }
}
