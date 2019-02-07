package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;


import android.content.Context;
import java.util.Date;

public class CallReceiver extends PhoneCallReceiver {
    public static boolean onCall=  false;

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        System.out.println(" aaaaaaaaaaaa123"); onCall = true;
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {onCall = true;
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {onCall = false;
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {onCall = false;
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {onCall = false;
    }

}