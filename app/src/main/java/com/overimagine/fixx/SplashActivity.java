package com.overimagine.fixx;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import com.overimagine.fixx.Util.NotificationUtil;

import static java.lang.Thread.sleep;

public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getPermission();

        if (checkPermission()) {
            goToMain GoToMain = new goToMain();
            GoToMain.start();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.createChannel(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: " + grantResults.length);
                    for (int i = 0; i < grantResults.length; i++) {
                        Log.d(TAG, "onRequestPermissionsResult: " + grantResults[i]);
                    }

                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                    if (!checkPermission())
                        deniedPermission();
                    else {
                        try {
                            sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                    deniedPermission();
                }
        }
    }

    public void deniedPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한이 없습니다.")        // 제목 설정
                .setMessage("앱을 종료합니다.")
                .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                });

        AlertDialog alertDialog = builder.create();    // 알림창 객체 생성
        alertDialog.show();

    }

    private class goToMain extends Thread {

        @Override
        public void run() {
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

    private void getPermission() {
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED
                || getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                || getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
        }
    }
}