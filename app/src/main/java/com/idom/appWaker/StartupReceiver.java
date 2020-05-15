package com.idom.appWaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

import static com.idom.appWaker.AlarmManagerCreator.SHARED_PREFS_NAME;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ReactNativeAppWaker", "StartupReceiver onReceive! ");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
        Map<String, ?> all = sharedPreferences.getAll();
        for (String alarmId : all.keySet()) {
            AlarmManagerCreator alarmManagerCreator = new AlarmManagerCreator();
            double timestamp = sharedPreferences.getLong(alarmId, -1);
            Log.i("ReactNativeAppWaker", "rescheduling alarm: " + alarmId + ", timestamp:" + timestamp);
            alarmManagerCreator.setAlarm(context, alarmId, timestamp);
        }
    }
}
