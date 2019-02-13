package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
        lastDataUsage = getValueString("data_usage", context).equals("") ? "0.00" : getValueString("data_usage", context);
        mStatus = getString("SERVICE");
        if ("1".equals(mStatus)) {
            Intent lockscreenService = new Intent(context, LockscreenService.class);
            ContextCompat.startForegroundService(context, lockscreenService);
            save("trigger_data_usage", "");
            save("first_boot", "");
        }
    }
    public String getString(String key) {
        spf = PreferenceManager.getDefaultSharedPreferences(mContext);
        return spf.getString(key, "");
    }
    public void save(String key, String value) {

        spf = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.apply();
    }
}
