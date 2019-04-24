package com.overimagine.fixx.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.overimagine.fixx.R;
import com.overimagine.fixx.Util.CallLogUtil;
import com.overimagine.fixx.Util.SimSlotUtil;

import androidx.core.app.NotificationCompat;


public class AutoFixService extends Service {
    public AutoFixService() {
    }

    private static final String TAG = "AutoFixService";
    private TelephonyManager manager;

    CallLogUtil mCallLogUtil;
    SimSlotUtil mPhoneUtil;

    @Override
    public void onCreate() {
        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d(TAG, "onCreate: Start PhoneStateListener");

        mCallLogUtil = new CallLogUtil(this);
        mPhoneUtil = new SimSlotUtil(this);
//        StartNotification();
        startForeground(0, new Notification());
        super.onCreate();
    }

    private void StartNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setContentTitle("자동 변환 서비스가 시작되었습니다.")
                .setContentText(mPhoneUtil.getSimSlotStatus(false))
//                .setSmallIcon(R.drawable.noti_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setCategory(Notification.CATEGORY_MESSAGE)
//                .setPriority(Notification.PRIORITY_HIGH);
                .setPriority(Notification.PRIORITY_DEFAULT);
//                .setVisibility(Notification.VISIBILITY_PUBLIC);
        Log.d(TAG, "onCreate: Create Notification");

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(2, builder.build());
        Log.d(TAG, "onCreate: Notify");

        if (mCallLogUtil.getErrorLogSize() != 0)
            mCallLogUtil.run(false);
    }


    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.i(TAG, "onCallStateChanged: " + incomingNumber);
            if (state == TelephonyManager.CALL_STATE_IDLE)
                if (!incomingNumber.isEmpty()) {
                    try {
                        Thread.sleep(3000);
                        if (mCallLogUtil.getErrorLogSize() != 0)
                            mCallLogUtil.run(true);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onCallStateChanged: " + incomingNumber);

                }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
