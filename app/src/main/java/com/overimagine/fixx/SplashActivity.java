package com.overimagine.fixx;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.overimagine.fixx.Util.NotificationUtil;
import com.overimagine.fixx.Util.SimSlotUtil;

public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;
    private SimSlotUtil mSimSlotUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Init SimSlotUtil
        mSimSlotUtil = new SimSlotUtil(this);

        // Init SharedPreference
        mSharedPreference = getSharedPreferences("PhoneInfo", MODE_PRIVATE);
        mEditor = mSharedPreference.edit();

        // Create Notification Channel
        NotificationUtil.createChannel(this);

        // Check Permission
        if (checkPermission()) {
            new InitThread().start();
        } else
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 100);
    }

    private boolean checkPermission() {
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED
                && getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
                && getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int j = 0;
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                if (++j == 3) new InitThread().start();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("권한이 없습니다.")
                        .setMessage("앱을 종료합니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                            }
                        }).create();
                alertDialog.show();
            }
        }
    }

    private class InitThread extends Thread {
        @Override
        public void run() {
            if (mSimSlotUtil.isMultiSimEnabled()) {
                mEditor.putBoolean("MultiSimEnabled", mSimSlotUtil.isMultiSimEnabled());
                mEditor.putBoolean("SIM_SLOT_1", mSimSlotUtil.isSim1Enabled());
                mEditor.putBoolean("SIM_SLOT_2", mSimSlotUtil.isSim2Enabled());
                mEditor.putString("MSISDN_1", mSimSlotUtil.getSim1MSISDN());
                mEditor.putString("MSISDN_2", mSimSlotUtil.getSim2MSISDN());
            } else {
                if (mSimSlotUtil.isSim1Enabled()) {
                    mEditor.putBoolean("MultiSimEnabled", mSimSlotUtil.isMultiSimEnabled());
                    mEditor.putBoolean("SIM_SLOT_1", mSimSlotUtil.isSim1Enabled());
                    mEditor.putBoolean("SIM_SLOT_2", false);
                    mEditor.putString("MSISDN_1", mSimSlotUtil.getSim1MSISDN());
                    mEditor.putString("MSISDN_2", null);
                } else {
                    mEditor.putBoolean("MultiSimEnabled", mSimSlotUtil.isMultiSimEnabled());
                    mEditor.putBoolean("SIM_SLOT_1", false);
                    mEditor.putBoolean("SIM_SLOT_2", mSimSlotUtil.isSim2Enabled());
                    mEditor.putString("MSISDN_1", null);
                    mEditor.putString("MSISDN_2", mSimSlotUtil.getSim2MSISDN());
                }
            }
            mEditor.commit();

            try {
                sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

            super.run();
        }
    }
}