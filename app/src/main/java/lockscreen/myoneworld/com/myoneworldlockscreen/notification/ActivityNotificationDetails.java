package lockscreen.myoneworld.com.myoneworldlockscreen.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.webviews.ActivityWebView;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.getValueString;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.save;

public class ActivityNotificationDetails extends AppCompatActivity {
    private int notifID = 0;
    private String message;
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    private void init() {
        TextView title = findViewById(R.id.notif_title);
        TextView content = findViewById(R.id.notif_content);
        ImageView extraContent = findViewById(R.id.extra_content);

        title.setText("NOTIFICATION_ID : " + notifID);
        content.setText(message);

        new NotificationDAO().readNotification(mContext,getValueString("ACCESS_TOKEN",mContext),Integer.toString(notifID));
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NOTIFICATION_ID")) {
                if("".equals(intent.getExtras().getString("LINK"))) {
                    setContentView(R.layout.activity_notification_details);
                    // extract the extra-data in the Notification
                    notifID = intent.getExtras().getInt("NOTIFICATION_ID");
                    message = intent.getExtras().getString("NOTIFICATION_MESSAGE");
                    save("NOTIFICATION_" + intent.getExtras().getInt("NOTIFICATION_ID"), "1", mContext);
                    init();
                }else{
                    String fbKey = getValueString("FB_KEY",mContext).equals("null") ? "" : getValueString("FB_KEY",mContext);
                    String googleKey = getValueString("GOOGLE_KEY",mContext).equals("null") ? "" :getValueString("GOOGLE_KEY",mContext);
                    String twitterKey = getValueString("TWITTER_KEY",mContext).equals("null") ? "" :getValueString("TWITTER_KEY",mContext);
                    String crypt = getValueString("CRYPT",mContext);
                    String email = getValueString("EMAIL",mContext);
                    String link = intent.getExtras().getString("LINK");
                    switch (intent.getExtras().getInt("APP")){
                        case 1:
                            ActivityWebView.url = "https://mybarko.tech/#/transfer?email="+email+"&crypt="+crypt+"&facebook_key="+fbKey+"&google_key="+googleKey+"&twitter_key="+twitterKey+"&link="+link;
                            break;
                        case 2:
                            ActivityWebView.url = "https://mystorya.tech/#/transfer?email="+email+"&crypt="+crypt+"&facebook_key="+fbKey+"&google_key="+6+"&twitter_key="+twitterKey+"&link="+link;
                            break;
                        case 3:
                            ActivityWebView.url = "https://mylifestyle.tech/#/transfer?email="+email+"&crypt="+crypt+"&facebook_key="+fbKey+"&google_key="+googleKey+"&twitter_key="+twitterKey+"&link="+link;
                            break;
                    }
                    startActivity(new Intent(mContext,ActivityWebView.class));
                    finish();
                }
            }
        }
    }
}
