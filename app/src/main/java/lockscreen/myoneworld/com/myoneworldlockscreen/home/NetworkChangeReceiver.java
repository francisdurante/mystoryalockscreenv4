package lockscreen.myoneworld.com.myoneworldlockscreen.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Objects;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.globalMessageBox;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.settings.ActivitySettings;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NEW_VERSION_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.NEW_VERSION_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DATA_USAGE_MSG;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.DATA_USAGE_TITLE;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MSG_BOX_WARNING;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;

public class NetworkChangeReceiver extends BroadcastReceiver {
    ImageView header;
    Activity activity;
    public NetworkChangeReceiver(ImageView header, Activity activity){
        this.header = header;
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Animation myanim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        switch (Objects.requireNonNull(intent.getAction())){
            case ConnectivityManager.CONNECTIVITY_ACTION:
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected() || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()) {
                    header.startAnimation(myanim);
                    header.setBackgroundColor(Color.parseColor("#f89a1e"));
                    header.setImageResource(R.drawable.header_new_update_3);
                    header.setOnClickListener(v -> {
                        context.startActivity(new Intent(context, ActivitySettings.class));
                        activity.finish();
                    });
                    if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()){
                        globalMessageBox(context,DATA_USAGE_MSG,DATA_USAGE_TITLE,MSG_BOX_WARNING);
                        save("WIFI_ONLY","",context);
                        save("WIFI_OR_DATA","",context);
                        save("DO_NOT_DOWNLOAD","1",context);

                    }else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()){
                        save("WIFI_ONLY","1",context);
                        save("WIFI_OR_DATA","",context);
                        save("DO_NOT_DOWNLOAD","",context);
                    }
                    if("outdated".equalsIgnoreCase(getValueString("VERSION_ONLINE",context))){
                        globalMessageBox(context,NEW_VERSION_MSG,NEW_VERSION_TITLE,MSG_BOX_WARNING);
                    }
                }
                else {
                    header.startAnimation(myanim);
                    header.setBackgroundColor(Color.parseColor("#1db7fd"));
                    header.setImageResource(R.drawable.connection);
                    header.setClickable(false);
                }
                break;
        }
    }
}
