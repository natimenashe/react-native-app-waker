package com.idom.appWaker;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

/**
 * User: nmenashe
 * Date: 08-05-2020
 * Time: 22:32
 */
public class AlarmScheduledJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        String alarmID = params.getExtras().getString("alarmID");
        Log.i("ReactNativeAppWaker", "onStartJob");
        String packageName = getApplicationContext().getApplicationContext().getPackageName();
        Intent launchIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);

        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        launchIntent.putExtra("alarmID", alarmID);
        getApplicationContext().startActivity(launchIntent);
        Log.i("ReactNativeAppWaker", "end startJob, alarmId: "+alarmID);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
