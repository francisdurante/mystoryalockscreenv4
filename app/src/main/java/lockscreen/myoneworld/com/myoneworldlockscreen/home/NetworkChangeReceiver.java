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
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;
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
                        globalMessageBox(context,"Using Mobile Data Connection, may cause data charges","Data Usage",MSG_BOX_WARNING);

                    }
                    if("outdated".equalsIgnoreCase(getValueString("VERSION_ONLINE",context))){
                        globalMessageBox(context,"New Version is now available in Google Play Store. Please update to continue using the lockscreen.","Application Update",MSG_BOX_WARNING);
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
