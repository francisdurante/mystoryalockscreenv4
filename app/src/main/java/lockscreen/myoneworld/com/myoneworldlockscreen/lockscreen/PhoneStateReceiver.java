package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import static lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.ActivityLockscreen.article_id;
import static lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.ActivityLockscreen.ads_count;
import static lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen.ActivityLockscreen.totalSize;
import android.telephony.TelephonyManager;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.freeMemory;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.isActivityRunning;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Utility.startJobService;

public class PhoneStateReceiver extends BroadcastReceiver {

    public static boolean offScreen = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        freeMemory();
        offScreen = false;
        new ActivityLockscreen(article_id);
        if (null != context) {
            boolean onCallOrRinging = false;
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            boolean onCall = tManager.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK ;
            boolean onRinging = tManager.getCallState() == TelephonyManager.CALL_STATE_RINGING;
            boolean isPhoneIdle = tManager.getCallState() == TelephonyManager.CALL_STATE_IDLE;

            if(onCall || onRinging){
                onCallOrRinging = true;
            }
            System.out.println("CALL STATUS IN CALL : " + onCallOrRinging);
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && !onCallOrRinging) {
                offScreen = isPhoneIdle;
                if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
                    if(!isActivityRunning) {
                        Intent screenIdle = new Intent(context, ActivityLockscreen.class);
                        screenIdle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(screenIdle);
                    }
                }else {
                    startJobService(context);
                }
            } else {
                offScreen = false;
            }

            freeMemory();
            ads_count++;
            if(totalSize == -1){
                ads_count = 0;
            }else if(ads_count > totalSize){
                ads_count = 0;
            }
        }
    }
}
