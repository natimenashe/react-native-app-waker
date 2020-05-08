package com.idom.appWaker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.facebook.react.bridge.*;
import com.idom.appWaker.permissions.PermissionsManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.app.AlarmManager.RTC_WAKEUP;


public class WakerModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;


    public WakerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AppWaker";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public final void setAlarmWorker(String id, double timestamp) {
        if(Build.MANUFACTURER.equalsIgnoreCase("xiaomi")){
            Log.i("ReactNativeAppWaker", "xiaomi device detected");
            setAlarm(id, timestamp, false);
            return;
        }
        Log.i("ReactNativeAppWaker", "in setAlarmWorker");
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.put("alarmID", id);
        OneTimeWorkRequest wakerAlarmRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                .setInputData(dataBuilder.build())
                //.setInitialDelay(5000, TimeUnit.MILLISECONDS)
                .setInitialDelay((long)timestamp - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();

        Context applicationContext = reactContext.getApplicationContext();
        WorkManager.getInstance(applicationContext).enqueueUniqueWork(id, ExistingWorkPolicy.REPLACE, wakerAlarmRequest);
    }

    @ReactMethod
    public final void clearAlarmWorker(String id) {
        if(Build.MANUFACTURER.equalsIgnoreCase("xiaomi")){
            Log.i("ReactNativeAppWaker", "xiaomi device detected");
            clearAlarm(id);
            return;
        }
        Log.i("ReactNativeAppWaker", "$$$ in clearAlarmWorker");
        Context applicationContext = reactContext.getApplicationContext();
        WorkManager.getInstance(applicationContext).cancelUniqueWork(id);
    }

    @ReactMethod
    public final void setAlarm(String id, double timestamp, boolean inexact) {
        createJob(id, (long) timestamp);
//        Log.i("ReactNativeAppWaker", "setAlarmClock# alarm manager");
//        PendingIntent pendingIntent = createPendingIntent(id);
//        long timestampLong = (long) timestamp; // React Bridge doesn't understand longs
//        getAlarmManager().setAlarmClock(new AlarmManager.AlarmClockInfo(timestampLong, pendingIntent), pendingIntent);
    }

    @ReactMethod
    public final void clearAlarm(String id) {
        Log.i("ReactNativeAppWaker", "in clearAlarm manager");
        PendingIntent pendingIntent = createPendingIntent(id);
        getAlarmManager().cancel(pendingIntent);
    }

    @ReactMethod
    public final void isPermissionWindowNavigationNeeded(final Promise promise) {
        Log.i("ReactNativeAppWaker", String.format("checking if permissions windows need to be displayed. build: %s, canDrawOverlay: %s", Build.VERSION.SDK_INT,
                Settings.canDrawOverlays(getReactApplicationContext())));
        WritableMap map = Arguments.createMap();
        boolean permissionRequired = !Settings.canDrawOverlays(getReactApplicationContext()) && Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1;
        map.putBoolean("permissionRequired", permissionRequired);
        map.putString("manufacturer", Build.MANUFACTURER);
        promise.resolve(map);

    }

    @ReactMethod
    public final void navigateToPermissionsWindow() {
        PermissionsManager.navigateToPermissionsWindow(getReactApplicationContext(), getCurrentActivity());
    }

    private PendingIntent createPendingIntent(String id) {
        Context context = getReactApplicationContext();
        // create the pending intent
        Intent intent = new Intent(context, AlarmReceiver.class);
        // set unique alarm ID to identify it. Used for clearing and seeing which one fired
        // public boolean filterEquals(Intent other) compare the action, data, type, package, component, and categories, but do not compare the extra
        intent.setData(Uri.parse("id://" + id));
        intent.setAction(String.valueOf(id));
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void createJob(String alarmId, long timestamp) {
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString("alarmID",alarmId);
        ComponentName serviceComponent = new ComponentName(getReactApplicationContext(), AlarmScheduledJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(timestamp - System.currentTimeMillis()); // wait at least
        //builder.setOverrideDeadline(5 * 1000); // maximum delay
        builder.setExtras(persistableBundle);
        JobScheduler jobScheduler = getReactApplicationContext().getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getReactApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }
}
