package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.MYONEWORLD;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.*;
import static lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.PhoneStateReceiver.offScreen;
import lockscreen.myoneworld.com.myoneworldlockscreen.R;
import lockscreen.myoneworld.com.myoneworldlockscreen.home.ActivityHome;

public class LockscreenService extends Service {
    NotificationManager nm;
    NotificationCompat.Builder notification;
    PhoneStateReceiver mLockscreenReceiver;
    Context mContext = this;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        mLockscreenReceiver = new PhoneStateReceiver();
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainActivity = new Intent(getApplicationContext(), ActivityHome.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, mainActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        notification = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                .setSmallIcon(R.drawable.logo_all_white)
                .setContentTitle(MYONEWORLD)
                .setContentIntent(pendingIntent)
                .setSound(null);
        startForeground(1, notification.build());
        setActivityRunning(false);
        super.onCreate();
    }

    @SuppressLint("InvalidWakeLockTag")
    public int onStartCommand(Intent intent, int flags, int startId) {
        freeMemory();
        offScreen = false;
        stateReceiver(true);
        if(!isActivityRunning) {
            if (null != intent) {
                Intent lockscreen = new Intent(mContext, ActivityLockscreen.class);
                lockscreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lockscreen);
                freeMemory();
            }
        }else{
            LockscreenDAO lockscreenDAO = new LockscreenDAO(mContext);
            lockscreenDAO.getArchivedStory();
            lockscreenDAO.newApiMyStoryaContent();
            getFileInFolder(mContext);
        }
        return LockscreenService.START_STICKY;
    }

    private void stateReceiver(boolean isStartRecever) {
        if (isStartRecever) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mLockscreenReceiver, filter);
        } else {
            if (null != mLockscreenReceiver) {
                unregisterReceiver(mLockscreenReceiver);
            }
        }
    }
    @Override
    public void onDestroy() {
        stateReceiver(false);
    }

}
