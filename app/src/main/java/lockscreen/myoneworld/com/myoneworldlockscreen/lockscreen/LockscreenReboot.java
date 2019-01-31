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
    Context mContext;

    @Override
    public void onReceive(Context context , Intent arg1) {
        mContext = context;
        lastDataUsage = getValueString("data_usage",mContext).equals("") ? "0.00" : getValueString("data_usage",mContext);
        mStatus = getValueString("SERVICE",mContext);
        if ("1".equals(mStatus)) {
            Intent phoneStateService = new Intent(mContext, LockscreenService.class);
            ContextCompat.startForegroundService(mContext, phoneStateService);
            save("trigger_data_usage","",mContext);
            save("first_boot","",mContext);
        }
    }


}
