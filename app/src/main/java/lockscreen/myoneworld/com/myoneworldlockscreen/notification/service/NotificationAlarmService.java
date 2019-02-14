package lockscreen.myoneworld.com.myoneworldlockscreen.notification.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.notification.ActivityNotificationDetails;
import lockscreen.myoneworld.com.myoneworldlockscreen.webviews.ActivityWebView;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MYPHONE_SHOP;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MY_LIFE_URL;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MY_STORYA_URL;

public class NotificationAlarmService extends BroadcastReceiver {

    private static final String CHANNEL_ID = "lockscreen.notification.channelId";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getExtras().getInt("APP")){
            case 1:
                ActivityWebView.url = MYPHONE_SHOP;
                break;
            case 2:
                ActivityWebView.url = MY_STORYA_URL;
                break;
            case 3:
                ActivityWebView.url = MY_LIFE_URL;
                break;
            default:
                ActivityWebView.url = MYPHONE_SHOP;
                break;
        }
        Intent notificationIntent = new Intent(context, ActivityNotificationDetails.class);
        notificationIntent.putExtra("NOTIFICATION_ID",intent.getExtras().getInt("NOTIF_ID"));
        notificationIntent.putExtra("NOTIFICATION_MESSAGE",intent.getExtras().getString("NOTIF_MESSAGE"));
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context,intent.getExtras().getInt("NOTIF_ID"),notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ActivityNotificationDetails.class);
        stackBuilder.addNextIntent(notificationIntent);

//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        Notification notification = builder.setContentTitle("my|ONEworld Notification")
                .setContentText(intent.getExtras().getString("NOTIF_MESSAGE"))
                .setTicker("New Notification")
                .setSmallIcon(R.drawable.my_storya_1)
                .setContentIntent(pendingNotificationIntent).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
//        Intent notificationIntent = new Intent(context, ActivityNotificationDetails.class);
//        notificationIntent.putExtra("NOTIFICATION_ID", intent.getExtras().getInt("NOTIF_ID"));
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context,notificationIndex,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(getApplicationContext(), notificationTitle, intent.getExtras().getInt("NOTIF_ID"), pendingNotificationIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context,intent.getExtras().getInt("NOTIF_ID"),notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "my|ONEworld Notification",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(intent.getExtras().getInt("NOTIF_ID"), notification);
    }
}
