package com.idom.appWaker.permissions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.facebook.react.bridge.ReactApplicationContext;

/**
 * User: nmenashe
 * Date: 09-04-2020
 * Time: 10:09
 */
public class PermissionsManager {

    public static void navigateToPermissionsWindow(ReactApplicationContext reactApplicationContext, Activity currentActivity){
        Log.i("ReactNativeAppWaker", String.format("navigating to permissions. build: %s, canDrawOverlay: %s", Build.VERSION.SDK_INT,
                Settings.canDrawOverlays(reactApplicationContext)));
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + currentActivity.getPackageName()));
        currentActivity.startActivityForResult(intent,0);
    }
}
