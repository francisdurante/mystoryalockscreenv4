package lockscreen.myoneworld.com.myoneworldlockscreen.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Objects;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;

public class NetworkReceiver extends BroadcastReceiver {
    LinearLayout header;
    Activity activity;
    SignInButton google;
    LoginButton facebook;
    TwitterLoginButton twitter;
    Button myStorya;
    Button login;
    int trigger;
    public NetworkReceiver(LinearLayout header, Activity activity, SignInButton googleB, LoginButton facebookB, TwitterLoginButton twitterB, Button myStoryaB, int arg){
        this.header = header;
        this.activity = activity;
        this.google = googleB;
        this.facebook = facebookB;
        this.twitter = twitterB;
        this.myStorya = myStoryaB;
        this.trigger = arg;
    }
    public NetworkReceiver(LinearLayout header, Activity activity,Button loginB, int arg) {
        this.header = header;
        this.activity = activity;
        this.login = loginB;
        this.trigger = arg;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())){
            case ConnectivityManager.CONNECTIVITY_ACTION:
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                    header.setVisibility(View.GONE);
                    enableDisableButtons(true);
                }
                else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()){
                    header.setVisibility(View.GONE);
                    enableDisableButtons(true);
                }
                else {
                    header.setVisibility(View.VISIBLE);
                    enableDisableButtons(false);
                }
                break;
        }
    }

    private void enableDisableButtons(boolean status){
        if(trigger == 1) {
            google.setEnabled(status);
            facebook.setEnabled(status);
            twitter.setEnabled(status);
            myStorya.setEnabled(status);
        }else{
            login.setEnabled(status);
        }
    }
}
