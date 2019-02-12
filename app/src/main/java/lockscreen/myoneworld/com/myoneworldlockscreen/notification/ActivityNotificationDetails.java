package lockscreen.myoneworld.com.myoneworldlockscreen.notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;

public class ActivityNotificationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        init();
    }

    private void init(){
        Intent intent = new Intent();
        TextView title = findViewById(R.id.notif_title);
        TextView content = findViewById(R.id.notif_content);
        ImageView extraContent = findViewById(R.id.extra_content);

        title.setText("NOTIFICATION_ID : " + intent.getExtras().getInt("NOTIFICATION_ID"));

    }
}
