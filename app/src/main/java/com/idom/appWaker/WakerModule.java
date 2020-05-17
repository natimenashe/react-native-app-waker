package com.idom.appWaker;


import android.content.Context;
import android.os.Build;
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
        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
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
                .setInitialDelay((long) timestamp - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();

        Context applicationContext = reactContext.getApplicationContext();
        WorkManager.getInstance(applicationContext).enqueueUniqueWork(id, ExistingWorkPolicy.REPLACE, wakerAlarmRequest);
    }

    @ReactMethod
    public final void clearAlarmWorker(String id) {
        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
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
        AlarmManagerCreator alarmManagerCreator = new AlarmManagerCreator();
        alarmManagerCreator.setAlarm(getReactApplicationContext(), id, timestamp);
    }

    @ReactMethod
    public final void clearAlarm(String id) {
        AlarmManagerCreator alarmManagerCreator = new AlarmManagerCreator();
        alarmManagerCreator.clearAlarm(getReactApplicationContext(), id);
    }

    @ReactMethod
    public final void removeAllPersistedAlarms() {
        Log.i("ReactNativeAppWaker", "on remove persisted alarms! ");
        AlarmManagerCreator alarmManagerCreator = new AlarmManagerCreator();
        alarmManagerCreator.removeAllPersistedAlarms(getReactApplicationContext());
    }

    @ReactMethod
    public final void test(String id) {
        Log.i("ReactNativeAppWaker", "on test! ");
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


}
