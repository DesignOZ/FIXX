package com.overimagine.fixx;

import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.overimagine.fixx.Util.CallLogUtil;
import com.overimagine.fixx.Util.SimSlotUtil;
import com.overimagine.fixx.Util.Util;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class SettingView extends PreferenceFragment implements OnPreferenceClickListener {
    private Uri UriCalls;
    Preference btn_autofix;
    Preference btn_fix;
    Preference btn_float;
    /* renamed from: c */
    private Cursor f9c;
    int calllog_size;
    Editor editor;
    int errorlog_size;
    CallLogUtil mCallLogUtil;
    SimSlotUtil mPhoneUtil;
    Util mUtil;
    SharedPreferences settings;
    private ContentValues values;

    /* renamed from: com.tistory.overimagine.voltecalllogfix.SettingView$1 */
    class C01951 implements OnClickListener {
        C01951() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            dialog.cancel();
        }
    }

    /* renamed from: com.tistory.overimagine.voltecalllogfix.SettingView$2 */
    class C01962 implements OnClickListener {
        C01962() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            new FixTask().execute(new Object[0]);
        }
    }

    /* renamed from: com.tistory.overimagine.voltecalllogfix.SettingView$3 */
    class C01973 implements OnClickListener {
        C01973() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            dialog.cancel();
        }
    }

    /* renamed from: com.tistory.overimagine.voltecalllogfix.SettingView$5 */
    class C01995 implements OnClickListener {
        C01995() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            SettingView.this.onObtainingPermissionOverlayWindow();
        }
    }

    /* renamed from: com.tistory.overimagine.voltecalllogfix.SettingView$6 */
    class C02006 implements OnClickListener {
        C02006() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            dialog.cancel();
        }
    }

    /* renamed from: com.tistory.overimagine.voltecalllogfix.SettingView$8 */
    class C02028 implements OnClickListener {
        C02028() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            dialog.cancel();
        }
    }

    private class FixTask extends AsyncTask {
        int fixlogs;
        ProgressDialog progressDialog;

        /* renamed from: com.tistory.overimagine.voltecalllogfix.SettingView$FixTask$1 */
        class C02031 implements OnClickListener {
            C02031() {
            }

            public void onClick(DialogInterface dialog, int whichButton) {
                SettingView.this.f9c.close();
            }
        }

        private FixTask() {
            this.fixlogs = 0;
        }

        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(SettingView.this.getActivity());
            this.progressDialog.setProgressStyle(1);
            this.progressDialog.setMessage("진행중입니다");
            this.progressDialog.setCancelable(false);
            this.progressDialog.setMax(SettingView.this.errorlog_size);
            this.progressDialog.show();
            super.onPreExecute();
        }

        protected Object doInBackground(Object[] objects) {
            if (SettingView.this.f9c.moveToFirst()) {
                do {
                    ProgressDialog progressDialog;
                    int i;
                    String num = SettingView.this.f9c.getString(SettingView.this.f9c.getColumnIndex("number"));
                    if (num.length() >= 21 && num.contains(SettingView.this.mPhoneUtil.getSim1MSISDN())) {
                        SettingView.this.mCallLogUtil.Fix(SettingView.this.f9c, SettingView.this.mPhoneUtil.getSim1MSISDN());
                        progressDialog = this.progressDialog;
                        i = this.fixlogs;
                        this.fixlogs = i + 1;
                        progressDialog.setProgress(i);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (SettingView.this.mPhoneUtil.isMultiSimEnabled() && num.length() >= 21 && num.contains(SettingView.this.mPhoneUtil.getSim2MSISDN())) {
                        SettingView.this.mCallLogUtil.Fix(SettingView.this.f9c, SettingView.this.mPhoneUtil.getSim2MSISDN());
                        progressDialog = this.progressDialog;
                        i = this.fixlogs;
                        this.fixlogs = i + 1;
                        progressDialog.setProgress(i);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                } while (SettingView.this.f9c.moveToNext());
            }
            return null;
        }

        protected void onPostExecute(Object o) {
            this.progressDialog.dismiss();
            Builder Line1builder = new Builder(SettingView.this.getActivity());
            Line1builder.setTitle("완료되었습니다.").setCancelable(true).setPositiveButton("확인", new C02031());
            Line1builder.create().show();
            super.onPostExecute(o);
            SettingView.this.refresh();
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mainactivity);
        this.mCallLogUtil = new CallLogUtil(getActivity());
        this.mPhoneUtil = new SimSlotUtil(getActivity());
        this.mUtil = new Util(getActivity());
        this.settings = getActivity().getSharedPreferences("settings", 0);
        this.editor = this.settings.edit();
        this.UriCalls = Uri.parse("content://call_log/calls");
        this.f9c = getActivity().getContentResolver().query(this.UriCalls, null, null, null, null);
        this.values = new ContentValues();
        this.btn_fix = findPreference("fix");
        this.btn_fix.setOnPreferenceClickListener(this);
        this.btn_autofix = findPreference("autofix");
        this.btn_autofix.setOnPreferenceClickListener(this);
        this.btn_float = findPreference("overlay");
//        this.btn_float.setOnPreferenceClickListener(this);
    }

    public void onResume() {
        super.onResume();
        this.calllog_size = this.mCallLogUtil.getCallLogSize();
        this.errorlog_size = this.mCallLogUtil.getErrorLogSize();
    }

    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        boolean z = true;
        switch (key.hashCode()) {
            case -1091287984:
                if (key.equals("overlay")) {
                    z = true;
                    break;
                }
                break;
            case -646308858:
                if (key.equals("autofix")) {
                    z = true;
                    break;
                }
                break;
            case 101397:
                if (key.equals("fix")) {
                    z = false;
                    break;
                }
                break;
        }
//        switch (z) {
//            case false:
//                if (this.errorlog_size == 0) {
//                    Toast.makeText(getActivity(), "통화목록 중 오류 항목이 없습니다.", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                AlertDialog.Builder Line1builder = new AlertDialog.Builder(getActivity());
//                Line1builder.setTitle((CharSequence) "주의").setMessage(getString(R.string.caution_summary)).setCancelable(true).setPositiveButton((CharSequence) "확인", new C01962()).setNegativeButton((CharSequence) "취소", new C01951());
//                Line1builder.create().show();
//                break;
//            case true:
//                final Intent autoFixIntent = new Intent(getActivity(), AutoFixService.class);
//                if (!this.mUtil.isServiceRunningCheck("AutoFixService")) {
//                    getActivity().startService(autoFixIntent);
//                    this.editor.putBoolean("autoFixBoot", true);
//                    this.editor.commit();
//                    StartNotification();
//                    refresh();
//                    break;
//                }
//                new AlertDialog.Builder(getActivity()).setTitle((CharSequence) "주의").setMessage(getString(R.string.auto_fix_close)).setCancelable(true).setPositiveButton((CharSequence) "확인", new OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Toast.makeText(SettingView.this.getActivity(), "서비스가 종료됩니다.", Toast.LENGTH_SHORT).show();
//                        SettingView.this.getActivity().stopService(autoFixIntent);
//                        SettingView.this.btn_autofix.setTitle(SettingView.this.getString(R.string.auto_fix));
//                        SettingView.this.editor.putBoolean("autoFixBoot", false);
//                        SettingView.this.editor.commit();
//                        SettingView.this.refresh();
//                    }
//                }).setNegativeButton((CharSequence) "취소", new C01973()).show();
//                break;
//            case true:
//                final Intent overlayIntent = new Intent(getActivity(), OverlayService.class);
//                AlertDialog.Builder overlayCloser;
//                if (!this.mUtil.isServiceRunningCheck("OverlayService")) {
//                    if (!Settings.canDrawOverlays(getActivity())) {
//                        overlayCloser = new AlertDialog.Builder(getActivity());
//                        overlayCloser.setTitle((CharSequence) "주의");
//                        overlayCloser.setMessage(getString(R.string.nopermission));
//                        overlayCloser.setCancelable(true);
//                        overlayCloser.setPositiveButton((CharSequence) "확인", new C01995());
//                        overlayCloser.setNegativeButton((CharSequence) "취소", new C02006());
//                        overlayCloser.create().show();
//                        break;
//                    }
//                    getActivity().startService(overlayIntent);
//                    this.editor.putBoolean("overlayBoot", true);
//                    this.editor.commit();
//                    StartNotification();
//                    refresh();
//                    break;
//                }
//                overlayCloser = new AlertDialog.Builder(getActivity());
//                overlayCloser.setTitle((CharSequence) "주의");
//                overlayCloser.setMessage(getString(R.string.auto_fix_close));
//                overlayCloser.setCancelable(true);
//                overlayCloser.setPositiveButton((CharSequence) "확인", new OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Toast.makeText(SettingView.this.getActivity(), "서비스가 종료됩니다.", Toast.LENGTH_SHORT).show();
//                        SettingView.this.getActivity().stopService(overlayIntent);
//                        SettingView.this.btn_autofix.setTitle(SettingView.this.getString(R.string.auto_fix));
//                        SettingView.this.editor.putBoolean("overlayBoot", false);
//                        SettingView.this.editor.commit();
//                        SettingView.this.refresh();
//                    }
//                });
//                overlayCloser.setNegativeButton((CharSequence) "취소", new C02028());
//                overlayCloser.create().show();
//                break;
//        }
        return false;
    }

    //    @TargetApi(23)
    public void onObtainingPermissionOverlayWindow() {
        startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getActivity().getPackageName())));
    }

    private void refresh() {
        startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    private void StartNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        Util util = new Util(getActivity());
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
//        builder.setContentTitle("FIXX 서비스가 시작되었습니다.").setContentText("실행중인 서비스 : " + s).setSmallIcon(R.drawable.noti_icon).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setAutoCancel(true).setWhen(System.currentTimeMillis()).setDefaults(-1).setCategory("msg").setContentIntent(PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), MainActivity.class).addFlags(536870912), 0)).setPriority(1).setVisibility(1);
        Log.d("ContentValues", "onCreate: Create Notification");
        ((NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE)).notify(2, builder.build());
        refresh();
        Log.d("ContentValues", "onCreate: Notify");
    }
}
