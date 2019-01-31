package lockscreen.myoneworld.com.myoneworldlockscreen.settings;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.home.ActivityHome;
import lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.LockscreenService;
import lockscreen.myoneworld.com.myoneworldlockscreen.login.ActivityLoginOptions;

public class ActivitySettings extends AppCompatActivity {

    Switch showHideDataUsage;
    Switch wifiOnly;
    Switch wifiAndData;
    Switch doNotDownload;
    SharedPreferences spf;
    Context mContext = this;
    LinearLayout generalSettingLinear;
    LinearLayout downloadSettingLinear;
    LinearLayout accountSettingLinear;
    TextView generalSettingSign;
    TextView downloadSettingSign;
    TextView accountSettingSign;
    Animation slideDown;
    Animation slideUp;
    boolean generalSettingShow = false;
    boolean downloadSettingShow = false;
    boolean accountSettingShow = false;
    TextView settingText;
    TextView downloadSettingText;
    TextView accountSettingText;
    TextView changePasswordText;
    TextView logoutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    public void init(){
        showHideDataUsage = findViewById(R.id.switch_hide_show);
        wifiOnly = findViewById(R.id.wifi_only);
        wifiAndData = findViewById(R.id.mobile_and_wifi);
        doNotDownload = findViewById(R.id.do_not_download);
        generalSettingLinear = findViewById(R.id.linear_general_setting);
        downloadSettingLinear = findViewById(R.id.linear_download);
        accountSettingLinear = findViewById(R.id.linear_account_setting);
        slideDown = AnimationUtils.loadAnimation(this,R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(this,R.anim.slide_up);
        downloadSettingSign = findViewById(R.id.download_setting_sign);
        accountSettingSign = findViewById(R.id.account_setting_sign);
        settingText = findViewById(R.id.setting_text);
        downloadSettingText = findViewById(R.id.download_setting);
        accountSettingText = findViewById(R.id.account_setting);
        changePasswordText = findViewById(R.id.change_password);
        logoutText = findViewById(R.id.logout);

        Typeface font = Typeface.createFromAsset(getAssets(), "font/Century_Gothic.ttf");

        downloadSettingText.setTypeface(font);
        accountSettingText.setTypeface(font);
        settingText.setTypeface(font);
        accountSettingSign.setTypeface(font);
        downloadSettingSign.setTypeface(font);
        wifiAndData.setTypeface(font);
        doNotDownload.setTypeface(font);
        wifiOnly.setTypeface(font);
        logoutText.setTypeface(font);
        changePasswordText.setTypeface(font);

        settingText.setText("Settings");
        settingText.setTextSize(12f);

        if(getValueString("WIFI_ONLY",mContext).equals("")){
            wifiOnly.setChecked(false);
        }else{
            wifiOnly.setChecked(true);
        }
        if(getValueString("WIFI_OR_DATA",mContext).equals("")){
            wifiAndData.setChecked(false);
        }else{
            wifiAndData.setChecked(true);
        }
        if(getValueString("DO_NOT_DOWNLOAD",mContext).equals("")){
            doNotDownload.setChecked(false);
        }else{
            doNotDownload.setChecked(true);
        }
        if(getValueString("WIFI_OR_DATA",mContext).equals("") && getValueString("DO_NOT_DOWNLOAD",mContext).equals("") && getValueString("WIFI_ONLY",mContext).equals("")){
            doNotDownload.setChecked(true);
            save("DO_NOT_DOWNLOAD","1",mContext);
            save("WIFI_ONLY","",mContext);
            save("WIFI_OR_DATA","",mContext);
        }
        if(getValueString("show_hide_data_usage",mContext).equals("")){
            showHideDataUsage.setChecked(false);
        }else{
            showHideDataUsage.setChecked(true);
        }
        showHideDataUsage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                save("show_hide_data_usage","show",mContext);
            }else{
                save("show_hide_data_usage","",mContext);
            }
        });
        wifiOnly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                wifiAndData.setChecked(false);
                doNotDownload.setChecked(false);
                save("WIFI_ONLY","1",mContext);
                save("WIFI_OR_DATA","",mContext);
                save("DO_NOT_DOWNLOAD","",mContext);
            }else{
                save("WIFI_ONLY","",mContext);
            }
            if(!wifiAndData.isChecked() && !doNotDownload.isChecked() && !wifiOnly.isChecked()){
                doNotDownload.setChecked(true);
                save("DO_NOT_DOWNLOAD","1",mContext);
                save("WIFI_ONLY","",mContext);
                save("WIFI_OR_DATA","",mContext);
            }
        });
        wifiAndData.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                showDialogBox("Enabling Wifi And Data may contain Data charges in downloading stories.\n",wifiAndData);
            }else{
                save("WIFI_OR_DATA","",mContext);
            }
            if(!wifiAndData.isChecked() && !doNotDownload.isChecked() && !wifiOnly.isChecked()){
                doNotDownload.setChecked(true);
                save("DO_NOT_DOWNLOAD","1",mContext);
                save("WIFI_ONLY","",mContext);
                save("WIFI_OR_DATA","",mContext);

            }
        });
        doNotDownload.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                wifiOnly.setChecked(false);
                wifiAndData.setChecked(false);
                save("DO_NOT_DOWNLOAD","1",mContext);
                save("WIFI_OR_DATA","",mContext);
                save("WIFI_ONLY","",mContext);
            }else{
                save("do_not_download","",mContext);
            }
            if(!wifiAndData.isChecked() && !doNotDownload.isChecked() && !wifiOnly.isChecked()){
                doNotDownload.setChecked(true);
                save("DO_NOT_DOWNLOAD","1",mContext);
                save("WIFI_ONLY","",mContext);
                save("WIFI_OR_DATA","",mContext);
            }
        });
    }
    private void showDialogBox(String Message, Switch setting){
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setIcon(mContext.getResources().getDrawable(R.drawable.new_logo_top));
        ab.setTitle(DATA_USAGE);
        final TextView et = new TextView(mContext);
        final TextView et2 = new TextView(mContext);
        et.setGravity(Gravity.LEFT);
        et.setPadding(35,0,0,10);
        et.setText(Message);
        String concat = "Estimate data usage of my|storya app\n" + getDataConsumption() + "\nDo you want to continue?";
        et2.setPadding(35,0,0,10);
        et.setGravity(Gravity.LEFT);
        et2.setTypeface(null,Typeface.BOLD);
        et2.setText(concat);
        layout.addView(et);
        layout.addView(et2);
        ab.setView(layout);
        ab.setCancelable(false);
        ab.setPositiveButton("OK", (dialog, which) -> {
            setting.setChecked(true);
            wifiOnly.setChecked(false);
            doNotDownload.setChecked(false);
            save("WIFI_OR_DATA","1",mContext);
            save("DO_NOT_DOWNLOAD","",mContext);
            save("WIFI_ONLY","",mContext);
        });
        ab.setNegativeButton("CANCEL", (dialog, which) -> {
            setting.setChecked(false);
            save("DO_NOT_DOWNLOAD","1",mContext);
            save("WIFI_OR_DATA","",mContext);
            save("WIFI_ONLY","",mContext);
        });
        AlertDialog a = ab.create();
        a.show();
    }

    public String getDataConsumption(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
        String total="";
        try {
            if(runningApps.size() > 0)
            {
                for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
                    if (runningApp.processName.equals("com.lockscreen.brown.brownlockscreen")) {
                        int app = runningApp.uid;
                        float received = TrafficStats.getUidRxBytes(app);//received amount of each app
                        String totalMB = String.format("%.2f", received * 0.000001);
                        double totalGB = Double.parseDouble(totalMB ) / 1024;
                        total = String.format("%.2f",totalGB) + "GB";
                    }
                }
            }
        }catch (Exception e){
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
            generateErrorLog(mContext,"err_log_" + getCurrentTime(),s);
        }
        return total;
    }
    public void logout(View v){
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                ab.setIcon(mContext.getResources().getDrawable(R.drawable.new_logo_top));
            }catch (Exception e){

            }
        }
        ab.setTitle("Log out");
        ab.setMessage("Do you want to logout my|storya?");
        ab.setPositiveButton("YES", (dialog, which) -> {
//            save("SHOW_POP_UP_DATA_USAGE","0",mContext);
            save("USER_ID","",mContext);
            save("FULL_NAME","",mContext);
            save("EMAIL","",mContext);
            save("ACCESS_TOKEN","",mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopJobService(mContext);
            }
            stopService(new Intent(mContext,LockscreenService.class));
            startActivity(new Intent(mContext,ActivityLoginOptions.class));
            save("SERVICE", "0",mContext);
            finish();
        });
        ab.setNegativeButton("NO", (dialog, which) -> {

        });
        AlertDialog a = ab.create();
        a.show();
    }
    public void accountSettingClick(View v){
        if(accountSettingShow) {
            accountSettingLinear.setVisibility(View.GONE);
            accountSettingSign.setText("+");
            accountSettingSign.setTypeface(null,Typeface.BOLD);
        }else{
            accountSettingLinear.setVisibility(View.VISIBLE);
            accountSettingSign.setText("-");
            accountSettingSign.setTypeface(null,Typeface.BOLD);
        }
        accountSettingShow = !accountSettingShow;
    }
    public void downloadSettingClick(View v){
        if(!isMyServiceRunning(LockscreenService.class,mContext)) {
            if (downloadSettingShow) {
                downloadSettingLinear.setVisibility(View.GONE);
                downloadSettingSign.setText("+");
                downloadSettingSign.setTypeface(null, Typeface.BOLD);
            } else {
                downloadSettingLinear.setVisibility(View.VISIBLE);
                downloadSettingSign.setText("-");
                downloadSettingSign.setTypeface(null, Typeface.BOLD);
            }
            downloadSettingShow = !downloadSettingShow;
        }
        else{
            globalMessageBox(mContext,"Please disable my|storya lockscreen","Download settings.",MSG_BOX_WARNING);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext,ActivityHome.class));
        finish();
        super.onBackPressed();
    }
}
