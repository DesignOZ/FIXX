package com.overimagine.fixx.Util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Horyeong Park on 2017-06-11.
 */

public class SimSlotUtil {
    private static final String TAG = "SimSlotUtil";
    private Context context;

    private List<SubscriptionInfo> subscriptionInfos;

    private ArrayList<String> MSISDNs;
    private ArrayList<Integer> SimSlots;

    private boolean Sim1Enabled = false;
    private boolean Sim2Enabled = false;

    public SimSlotUtil(Context mContext) {
        context = mContext;
//        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return TODO;
        }
        subscriptionInfos = SubscriptionManager.from(mContext).getActiveSubscriptionInfoList();
        SimSlots = new ArrayList<>();
        MSISDNs = new ArrayList<>();

        for (SubscriptionInfo subscriptionInfo : subscriptionInfos) {
            Log.i(TAG, "SIM Slot: " + (subscriptionInfo.getSimSlotIndex() + 1));
            Log.i(TAG, "SIM Slot: " + subscriptionInfo.getNumber());

            if (subscriptionInfo.getSimSlotIndex() == 0)
                Sim1Enabled = true;
            else
                Sim2Enabled = true;

            SimSlots.add(subscriptionInfo.getSimSlotIndex() + 1);

            if (subscriptionInfo.getNumber().length() > 11) {
                MSISDNs.add("010" + subscriptionInfo.getNumber().substring(subscriptionInfo.getNumber().length() - 8, subscriptionInfo.getNumber().length()));
            } else {
                MSISDNs.add(subscriptionInfo.getNumber());
            }
        }
    }

    public boolean isMultiSimEnabled() {
        return subscriptionInfos.size() > 1;
    }

    public boolean isSim1Enabled() {
        return Sim1Enabled;
    }

    public boolean isSim2Enabled() {
        return Sim2Enabled;
    }

    public String getSim1MSISDN() {
        if (isSim1Enabled())
            return MSISDNs.get(0);

        else return null;
    }

    public String getSim2MSISDN() {
        if (isSim2Enabled())
            if (isSim1Enabled())
                return MSISDNs.get(1);
            else
                return MSISDNs.get(0);

        else return null;
    }

    private String getSim1Slot() {
        if (isSim1Enabled())
            return SimSlots.get(0) + ":\t" + MSISDNs.get(0);

        else return null;
    }

    private String getSim2Slot() {
        if (isSim2Enabled())
            if (isSim1Enabled())
                return SimSlots.get(1) + ":\t" + MSISDNs.get(1);
            else
                return SimSlots.get(0) + ":\t" + MSISDNs.get(0);

        else return null;
    }


    public String getLine1Number() {
        return MSISDNs.get(0);
    }
}
