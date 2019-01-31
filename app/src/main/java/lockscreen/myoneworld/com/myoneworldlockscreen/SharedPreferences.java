package lockscreen.myoneworld.com.myoneworldlockscreen;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPreferences {

    static android.content.SharedPreferences spf;

    public static void save(String key, String value, Context context){
        spf = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.apply();
    }
    public static String getValueString(String key, Context context){
        spf = PreferenceManager.getDefaultSharedPreferences(context);
        return spf.getString(key, "");
    }
}
