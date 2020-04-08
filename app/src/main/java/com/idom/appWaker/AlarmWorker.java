package com.idom.appWaker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * User: nmenashe
 * Date: 10-03-2020
 * Time: 22:45
 */
public class AlarmWorker extends Worker {


    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String alarmID = getInputData().getString("alarmID");

        Context applicationContext = getApplicationContext();
        String packageName = applicationContext.getApplicationContext().getPackageName();
        Intent launchIntent = applicationContext.getPackageManager().getLaunchIntentForPackage(packageName);

        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        launchIntent.putExtra("alarmID", alarmID);

        applicationContext.startActivity(launchIntent);
        Log.i("ReactNativeAppWaker", "AlarmReceiver: Launching: " + packageName);
        return Result.success();
    }
}
