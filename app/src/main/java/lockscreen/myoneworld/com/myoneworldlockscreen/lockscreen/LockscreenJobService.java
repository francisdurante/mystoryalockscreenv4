package lockscreen.myoneworld.com.myoneworldlockscreen.lockscreen;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LockscreenJobService extends JobService {
    private boolean jobCancelled;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onStartJob(JobParameters params) {
        doInBackground(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled = true;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doInBackground(final JobParameters params){
        if(jobCancelled){
            return;
        }
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && "true".equals(params.getExtras().getString("connection")) && PhoneStateReceiver.offScreen){
            Intent lockscreenService = new Intent(this,LockscreenService.class);
            ContextCompat.startForegroundService(getApplicationContext(),lockscreenService);
            PhoneStateReceiver.offScreen = false;
        }
        jobFinished(params,true);
    }
}
