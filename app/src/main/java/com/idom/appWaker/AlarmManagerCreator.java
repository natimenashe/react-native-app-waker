package com.idom.appWaker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;


/**
 * User: nmenashe
 * Date: 16-05-2020
 * Time: 00:41
 */
public class AlarmManagerCreator {
    public final static String SHARED_PREFS_NAME = "WATCHME_PREFS";

    public void setAlarm(Context context, String id, double timestamp) {
        Log.i("ReactNativeAppWaker", "setAlarmClock# alarm manager");
        PendingIntent pendingIntent = createPendingIntent(context, id);
        persistAlarm(context, id, timestamp);
        long timestampLong = (long) timestamp; // React Bridge doesn't understand longs
        getAlarmManager(context).setAlarmClock(new AlarmManager.AlarmClockInfo(timestampLong, pendingIntent), pendingIntent);
    }

    public final void clearAlarm(Context context, String id) {
        Log.i("ReactNativeAppWaker", "in clearAlarm manager");
        PendingIntent pendingIntent = createPendingIntent(context, id);
        removePersistedAlarm(context, id);
        getAlarmManager(context).cancel(pendingIntent);
    }

    public void removeAllPersistedAlarms(Context context) {
        Log.i("ReactNativeAppWaker", "removing all persisted alarms");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }

    private PendingIntent createPendingIntent(Context context, String id) {
        // create the pending intent
        Intent intent = new Intent(context, AlarmReceiver.class);
        // set unique alarm ID to identify it. Used for clearing and seeing which one fired
        // public boolean filterEquals(Intent other) compare the action, data, type, package, component, and categories, but do not compare the extra
        intent.setData(Uri.parse("id://" + id));
        intent.setAction(String.valueOf(id));
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private void persistAlarm(Context context, String id, double timestamp) {
        Log.i("ReactNativeAppWaker", "persist alarm: " + id);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(id, (long) timestamp);
        edit.apply();
    }

    private void removePersistedAlarm(Context context, String id) {
        Log.i("ReactNativeAppWaker", "removing persisted alarm: " + id);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(id);
        edit.apply();
    }
}
