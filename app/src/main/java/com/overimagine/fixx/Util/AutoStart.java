package com.overimagine.fixx.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.overimagine.fixx.MainActivity;
import com.overimagine.fixx.R;
import com.overimagine.fixx.Service.AutoFixService;
import com.overimagine.fixx.Service.OverlayService;

public class AutoStart extends BroadcastReceiver {
    private static final String TAG = "AutoStart";
    Boolean autoFixBoot;
    Context context;
    SimSlotUtil mSimSlotUtil;
    Boolean overlayBoot;
    SharedPreferences settings;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.mSimSlotUtil = new SimSlotUtil(context);
        this.settings = context.getSharedPreferences("settings", 0);
        this.autoFixBoot = Boolean.valueOf(this.settings.getBoolean("autoFixBoot", false));
        this.overlayBoot = Boolean.valueOf(this.settings.getBoolean("overlayBoot", false));
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            if (this.autoFixBoot.booleanValue()) {
                context.startService(new Intent(context, AutoFixService.class));
                Log.d(TAG, "onReceive: AutoFix BOOT");
            }
            if (this.overlayBoot.booleanValue()) {
                context.startService(new Intent(context, OverlayService.class));
                Log.d(TAG, "onReceive: Overlay BOOT");
            }
            StartNotification();
        }
    }

    private void StartNotification() {
        Notification.Builder builder = new Notification.Builder(this.context);
        ServiceRunningCheck util = new ServiceRunningCheck(this.context);
        boolean a = util.isServiceRunningCheck("AutoFixService");
        boolean b = util.isServiceRunningCheck("OverlayService");
        String s = null;
        if ((a & b)) {
            s = "자동변환 서비스, 오버레이 서비스";
        } else if (a) {
            s = "자동변환 서비스";
        } else if (b) {
            s = "오버레이 서비스";
        }
        builder.setContentTitle("FIXX 서비스가 시작되었습니다.").setContentText("실행중인 서비스 : " + s).setSmallIcon(R.drawable.noti_icon).setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.mipmap.ic_launcher)).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(this.context, 0, new Intent(this.context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)).setWhen(System.currentTimeMillis()).setDefaults(-1).setCategory("msg").setPriority(1).setVisibility(1);
        Log.d(TAG, "onCreate: Create Notification");
        ((NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(2, builder.build());
        Log.d(TAG, "onCreate: Notify");
    }
}
