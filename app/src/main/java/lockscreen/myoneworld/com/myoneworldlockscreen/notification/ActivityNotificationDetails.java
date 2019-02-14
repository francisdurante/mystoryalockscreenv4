package lockscreen.myoneworld.com.myoneworldlockscreen.notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;

public class ActivityNotificationDetails extends AppCompatActivity {
    private int notifID = 0;
    private String message;
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


    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NOTIFICATION_ID")) {
                setContentView(R.layout.activity_notification_details);
                // extract the extra-data in the Notification
                notifID = intent.getExtras().getInt("NOTIFICATION_ID");
                message = intent.getExtras().getString("NOTIFICATION_MESSAGE");
                init();
            }
        }
    }
}
