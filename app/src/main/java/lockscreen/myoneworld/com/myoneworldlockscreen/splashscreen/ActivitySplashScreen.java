package lockscreen.myoneworld.com.myoneworldlockscreen.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import lockscreen.myoneworld.com.myoneworldlockscreen.AppUpdateChecker;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.briefing.ActivityBriefing;

public class ActivitySplashScreen extends AppCompatActivity {
    private ImageView img;
    Context mContext = this;
    View decorView;
    public static String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            currentVersion =  getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            new AppUpdateChecker().execute(mContext);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_splash_screen);
        img = findViewById(R.id.font);
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Animation myanim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        img.startAnimation(myanim);
        final Intent i = new Intent(this,ActivityBriefing.class);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                startActivity(i);
                finish();
                img = null;
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        img = null;
        decorView = null;
        super.onDestroy();
    }
}
