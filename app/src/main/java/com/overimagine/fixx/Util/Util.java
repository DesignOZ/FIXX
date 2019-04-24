package com.overimagine.fixx.Util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class Util {
    Context context;

    public Util(Context context) {
        this.context = context;
    }

    public boolean isServiceRunningCheck(String s) {
        String className = "com.tistory.overimagine.voltecalllogfix.Util." + s;
        for (RunningServiceInfo service : ((ActivityManager) this.context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
