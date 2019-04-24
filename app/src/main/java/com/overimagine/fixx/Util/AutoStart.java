package com.overimagine.fixx.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.overimagine.fixx.R;

public class AutoStart extends BroadcastReceiver {
    private static final String TAG = "AutoStart";

    Context context;
    SimSlotUtil mPhoneUtil;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        mPhoneUtil = new SimSlotUtil(context);

        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        // 수신된 action값이 시스템의 '부팅 완료'가 맞는지 확인..

        assert action != null;
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceIntent = new Intent(context, AutoFixService.class);
            context.startService(serviceIntent);
            StartNotification();
        }
    }

    private void StartNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("자동 변환 서비스가 시작되었습니다.")
                .setContentText(mPhoneUtil.getSimSlotStatus(false))
                .setSmallIcon(R.drawable.noti_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(Notification.VISIBILITY_PUBLIC);
        Log.d(TAG, "onCreate: Create Notification");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, builder.build());
        Log.d(TAG, "onCreate: Notify");

    }
}
