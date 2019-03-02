package lockscreen.myoneworld.com.myoneworldlockscreen.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    DrawerLayout drawerLayout;
    TextView fullName;
    TextView email;
    ImageView profilePic;
    Button serviceButton;
    public NetworkChangeReceiver(ImageView header, Activity activity){
        this.header = header;
        this.activity = activity;
    }
    public NetworkChangeReceiver(ImageView header, Activity activity,DrawerLayout drawerLayout,TextView fullName, TextView email,ImageView profilePic,Button serviceButton){
        this.header = header;
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.fullName = fullName;
        this.email = email;
        this.profilePic = profilePic;
        this.serviceButton = serviceButton;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Animation myanim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        switch (Objects.requireNonNull(intent.getAction())){
            case ConnectivityManager.CONNECTIVITY_ACTION:
                HomeDAO dao = new HomeDAO(context,((Activity)context));
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected() || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()) {
                    header.startAnimation(myanim);
                    header.setBackgroundColor(Color.parseColor("#3a5daa"));
                    header.setImageResource(R.drawable.new_header_blue_02192019);
                    header.setOnClickListener(v -> {
                        drawerLayout.openDrawer(Gravity.START);
//                        context.startActivity(new Intent(context, ActivitySettings.class));
//                        activity.finish();
                    });
                    if("outdated".equalsIgnoreCase(getValueString("VERSION_ONLINE",context))){
                        globalMessageBox(context,NEW_VERSION_MSG,NEW_VERSION_TITLE,MSG_BOX_WARNING,new AlertDialog.Builder(context).create());
                    }
                    dao.checkIfValidLogin(getValueString("ACCESS_TOKEN",context),drawerLayout,fullName,email,profilePic,serviceButton);
                }
                else {
                    header.startAnimation(myanim);
                    header.setBackgroundColor(Color.parseColor("#3a5daa"));
                    header.setImageResource(R.drawable.connection);
                    header.setClickable(false);
                    dao.checkIfValidLogin(getValueString("ACCESS_TOKEN",context),drawerLayout,fullName,email,profilePic,serviceButton);
                }
                break;
        }
    }
}
