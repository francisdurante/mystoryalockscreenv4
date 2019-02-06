package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import static lockscreen.myoneworld.com.myoneworldlockscreen.Constant.*;
import java.util.Date;

public class CallReceiver extends PhoneCallReceiver {
    public Activity activity;
    public static boolean onCall = false;

//    public CallReceiver(Activity activity){
//        this.activity = activity;
//    }
//    public CallReceiver(){
//
//    }
    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        onCall = true;
        System.out.println(number + " aaaaaaaaaaaaaaa");
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        onCall = true;
        System.out.println(number + " aaaaaaaaaaaaaaa");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        onCall = false;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        onCall = false;

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        onCall = false;
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void stopJobService() {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            JobScheduler scheduler = (JobScheduler) activity.getSystemService(activity.JOB_SCHEDULER_SERVICE);
//            scheduler.cancel(JOB_SCHEDULE_ID);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.O)
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void startJobService(Context ctx) {
//        ComponentName jobService = new ComponentName(ctx, LockscreenJobService.class);
//        PersistableBundle bundle = new PersistableBundle();
//        JobInfo info = new JobInfo.Builder(JOB_SCHEDULE_ID, jobService)
//                .setExtras(bundle)
//                .setPersisted(true)
//                .setPeriodic(PERIODIC)
//                .build();
//
//        JobScheduler scheduler = (JobScheduler) activity.getSystemService(activity.JOB_SCHEDULER_SERVICE);
//        scheduler.schedule(info);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void stopLockscreen(Context ctx){
//        activity.finish();
//        Intent lockscreenService = new Intent(ctx, LockscreenService.class);
//        activity.stopService(lockscreenService);
//        //stopJobService();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void startLockscreen(Context ctx){
//        Intent lockscreenService = new Intent(ctx, LockscreenService.class);
//        activity.startService(lockscreenService);
//        //startJobService(ctx);
//    }
}