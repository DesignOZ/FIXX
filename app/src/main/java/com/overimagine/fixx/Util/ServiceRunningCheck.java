package com.overimagine.fixx.Util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.util.Log;

public class ServiceRunningCheck {
    private static final String TAG = "Util/";
    Context context;

    public ServiceRunningCheck(Context context) {
        this.context = context;
    }

    public boolean isServiceRunningCheck(String ServiceName) {
        String className = "com.overimagine.fixx.Service." + ServiceName;
        for (RunningServiceInfo service : ((ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                Log.d(TAG, "isServiceRunningCheck: " + ServiceName + " is running!");
                return true;
            }
        }
        Log.d(TAG, "isServiceRunningCheck: " + ServiceName + " is not running!");
        return false;
    }
}
