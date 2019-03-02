package lockscreen.myoneworld.com.myoneworldlockscreen.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import lockscreen.myoneworld.com.myoneworldlockscreen.BuildConfig;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.Utility;
import lockscreen.myoneworld.com.myoneworldlockscreen.editprofile.EditProfileDAO;
import lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.LockscreenService;
import lockscreen.myoneworld.com.myoneworldlockscreen.notification.NotificationDAO;
import lockscreen.myoneworld.com.myoneworldlockscreen.settings.ActivitySettings;
import lockscreen.myoneworld.com.myoneworldlockscreen.webviews.ActivityWebView;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.CAMERA;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DEVELOPMENT_VERSION;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GALLERY;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_BOLD_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.GOTHIC_FONT_PATH;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LOGGING_OUT_MESSAGE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.LOGGING_OUT_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_ERROR;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NO_PHOTO;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.REQUEST_CODE_CAMERA;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.REQUEST_CODE_READ_STORAGE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.getVersionName;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.globalMessageBox;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.setFont;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.isMyServiceRunning;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.showPopUpWallet;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.stopJobService;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.createNotificationChannel;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.isNetworkAvailable;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NEW_VERSION_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NEW_VERSION_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.ENABLE_AUTO_START_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.AUTO_START_MSG_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_WARNING;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.STOP;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.START;
import static lockscreen.myoneworld.com.myoneworldlockscreen.notification.NotificationDAO.getCountUnread;

public class ActivityHome extends AppCompatActivity {
    Context mContext = this;
    ImageView header;
    Button serviceButton;
    private ViewPager viewPager;
    NetworkChangeReceiver ncr;
    public static String updateStatus;
    public final static int REQUEST_CODE = 1010;
    CallbackManager callbackManager;
    private TwitterAuthClient client;
    private MenuItem settings;
    private MenuItem aboutUs;
    private MenuItem logout;
    private MenuItem wallet;
    private MenuItem editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Utility utility = new Utility();
//        utility.scheduledNotification(mContext,"2019-02-12 15:44",1);
//        utility.immediateNotification(mContext,2);
//        TwitterConfig config = new TwitterConfig.Builder(mContext)
//                .logger(new DefaultLogger(Log.DEBUG))
//                .twitterAuthConfig(new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET))
//                .debug(true)
//                .build();
//        Twitter.initialize(config);
//        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
//                .getActiveSession();
//
//        shareWithTwitter(session);
        setContentView(R.layout.activity_home);
        initDrawer();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            globalMessageBox(mContext, ENABLE_AUTO_START_MSG, AUTO_START_MSG_TITLE, MSG_BOX_WARNING,new AlertDialog.Builder(mContext).create());
        }
        init();
    }

    public void init() {
        try {
            switch (updateStatus) {
                case "outdated":
                    globalMessageBox(mContext, NEW_VERSION_MSG, NEW_VERSION_TITLE, MSG_BOX_WARNING,new AlertDialog.Builder(mContext).create());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        iniDefaultSetting();
        getCountUnreadNotification(mContext);
        serviceButton.setTypeface(setFont(mContext, GOTHIC_FONT_PATH));

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
                Manifest.permission.READ_EXTERNAL_STORAGE,
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
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA);
            }
        }
        if (requestCode == REQUEST_CODE_READ_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_READ_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY);
            }
        }
    }

    private void showGPSDisabledAlertToUser() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            HomeDAO.location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            HomeDAO.location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }
        if (!gps_enabled && !network_enabled) {
            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(callGPSSettingIntent);
        } else {
            new HomeDAO(mContext, this).sendLocationTimer();
        }
    }

    private void iniDefaultSetting() {
        if (getValueString("WIFI_ONLY", mContext).equals("") && getValueString("WIFI_OR_DATA", mContext).equals("") && getValueString("DO_NOT_DOWNLOAD", mContext).equals("")) {
            save("DO_NOT_DOWNLOAD", "1", mContext);
        }
    }

//    private void shareWithFacebook(){
//        callbackManager = CallbackManager.Factory.create();
//        ShareDialog shareDialog = new ShareDialog(this);
//
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onSuccess(Sharer.Result result) {
//                System.out.println("success share!");
//            }
//
//            @Override
//            public void onCancel() {
//                System.out.println("cancel share!");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                System.out.println(error.toString());
//            }
//        });
//
//        if(ShareDialog.canShow(ShareLinkContent.class)){
//            shareDialog.show(new Utility().facebookShare("https://google.com"));
//        }
//    }
//    @Override
//    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        if (client != null)
//            client.onActivityResult(requestCode, resultCode, data);
//    }

//    private void shareWithTwitter(TwitterSession session){
//       new Utility().twitterShare("https://google.com",mContext,session);
//    }

    private void initDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_details);
        TextView fullName = navigationView.getHeaderView(0).findViewById(R.id.full_name);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.email_side);
        ImageView profilePicture = navigationView.getHeaderView(0).findViewById(R.id.profile_pic);

        new NotificationDAO().getAllUnreadNotificationInMyCrazySale(mContext, getValueString("ACCESS_TOKEN", mContext));
        profilePicture.setOnClickListener(v -> {
            EditProfileDAO dao = new EditProfileDAO();
            dao.getUserProfile(mContext, getValueString("ACCESS_TOKEN", mContext), false, true,false);
        });
        Typeface font = setFont(mContext, GOTHIC_FONT_PATH);
        settings = navigationView.getMenu().findItem(R.id.setting_drawer);
        aboutUs = navigationView.getMenu().findItem(R.id.about);
        logout = navigationView.getMenu().findItem(R.id.logout_side);
        wallet = navigationView.getMenu().findItem(R.id.wallet);
        editProfile = navigationView.getMenu().findItem(R.id.edit_profile);
        header = findViewById(R.id.header);

        TextView versionText = findViewById(R.id.version);
        TextView testText = findViewById(R.id.test_version);
        testText.setTypeface(font);
        versionText.setTypeface(font);
        fullName.setTypeface(font);
        email.setTypeface(font);
        versionText.setText(getVersionName(mContext));
        if (BuildConfig.DEBUG) {
            testText.setVisibility(View.VISIBLE);
        }
        editProfile.setOnMenuItemClickListener(menuClick);
        settings.setOnMenuItemClickListener(menuClick);
        aboutUs.setOnMenuItemClickListener(menuClick);
        logout.setOnMenuItemClickListener(menuClick);
        wallet.setOnMenuItemClickListener(menuClick);

        serviceButton = findViewById(R.id.stopService);
        ncr = new NetworkChangeReceiver(header, this, drawerLayout, fullName, email, profilePicture,
                serviceButton);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(ncr, intentFilter);
    }

    private MenuItem.OnMenuItemClickListener menuClick = item -> {
        switch (item.getItemId()) {
            case R.id.setting_drawer:
                startActivity(new Intent(mContext, ActivitySettings.class));
                break;
            case R.id.about:
                ActivityWebView.url = "https://mystorya.tech";
                startActivity(new Intent(mContext, ActivityWebView.class));
                finish();
                break;
            case R.id.logout_side:
                globalMessageBox(mContext, LOGGING_OUT_MESSAGE, LOGGING_OUT_TITLE, MSG_BOX_WARNING,new AlertDialog.Builder(mContext).create());
                break;
            case R.id.wallet:
                showPopUpWallet(mContext, getValueString("ACCESS_TOKEN", mContext),new AlertDialog.Builder(mContext).create());
                break;
            case R.id.edit_profile:
                EditProfileDAO dao = new EditProfileDAO();
                dao.getUserProfile(mContext,getValueString("ACCESS_TOKEN",mContext),false,false,true);
                break;
        }
        return true;
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (null != photo) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    Utility.customLoadProfilePic(mContext, photo, R.drawable.com_facebook_profile_picture_blank_square);
                } else {
                    globalMessageBox(mContext, NO_PHOTO, MSG_BOX_ERROR.toUpperCase(), MSG_BOX_ERROR,new AlertDialog.Builder(mContext).create());
                }
            }
        }
        if (requestCode == GALLERY) {

            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Utility.customLoadProfilePic(mContext, bitmap, R.drawable.com_facebook_profile_picture_blank_square);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCountUnreadNotification(Context context) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask backtask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                       if(!"".equals(getValueString("ACCESS_TOKEN",context))) {
                           getCountUnread(context, getValueString("ACCESS_TOKEN", context));
                       }else{
                           handler.removeCallbacks(this);
                       }
                    } catch (Exception e) {
                    }
                });
            }
        };
        timer.schedule(backtask , 0, 5000);
    }
}
