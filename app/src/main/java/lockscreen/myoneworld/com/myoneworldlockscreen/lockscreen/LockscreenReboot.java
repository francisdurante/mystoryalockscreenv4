package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import static lockscreen.myoneworld.com.myoneworldlockscreen.SharedPreferences.*;
public class LockscreenReboot extends BroadcastReceiver {
    static SharedPreferences spf;
    public static String lastDataUsage;
    String mStatus;

    @Override
    public void onReceive(Context context , Intent arg1) {
        lastDataUsage = getValueString("data_usage", context).equals("") ? "0.00" : getValueString("data_usage", context);
        mStatus = getValueString("SERVICE", context);
        if (Intent.ACTION_BOOT_COMPLETED.equals(arg1.getAction())) {
            if ("1".equals(mStatus)) {
                Intent lockscreenService = new Intent(context, LockscreenService.class);
                ContextCompat.startForegroundService(context, lockscreenService);
                save("trigger_data_usage", "", context);
                save("first_boot", "", context);

            }
        }
    }


}
