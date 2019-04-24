package com.overimagine.fixx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.overimagine.fixx.Util.CallLogUtil;
import com.overimagine.fixx.Util.ServiceRunningCheck;
import com.overimagine.fixx.Util.SimSlotUtil;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "MainActivity";
    int ErrorLogSize;
    Boolean OverlayBoot;
    Boolean autoFixBoot;
    Editor editor;
    ImageView img_autofix;
    ImageView img_autofix_boot;
    ImageView img_overlay;
    ImageView img_overlay_boot;
    CallLogUtil mCallLogUtil;
    SimSlotUtil mSimSlotUtil;
    ServiceRunningCheck mUtil;
    SharedPreferences settings;
    TextView txt_count;
    TextView txt_count_ea;
    TextView txt_exist;
    TextView txt_sim1;
    TextView txt_sim2;
    String version;

    /* renamed from: com.tistory.overimagine.voltecalllogfix.MainActivity$1 */
    class C01921 implements DialogInterface.OnClickListener {
        C01921() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    }

    /* renamed from: com.tistory.overimagine.voltecalllogfix.MainActivity$2 */
    class C01932 implements DialogInterface.OnClickListener {
        C01932() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            MainActivity.this.editor.putString("Version", MainActivity.this.version);
            MainActivity.this.editor.commit();
            dialogInterface.cancel();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.mSimSlotUtil = new SimSlotUtil(this);
        this.mCallLogUtil = new CallLogUtil(this);
        this.mUtil = new ServiceRunningCheck(this);
        this.settings = getSharedPreferences("settings", 0);
        this.editor = this.settings.edit();
        this.txt_count = (TextView) findViewById(R.id.txt_main_count);
        this.txt_count_ea = (TextView) findViewById(R.id.txt_main_count_ea);
        this.txt_exist = (TextView) findViewById(R.id.txt_main_isErrorExist);
        this.txt_sim1 = (TextView) findViewById(R.id.txt_main_sim1);
        this.txt_sim2 = (TextView) findViewById(R.id.txt_main_sim2);
        this.img_autofix = (ImageView) findViewById(R.id.img_main_autofix);
        this.img_autofix_boot = (ImageView) findViewById(R.id.img_main_autofix_boot);
        this.img_overlay = (ImageView) findViewById(R.id.img_main_overlay);
        this.img_overlay_boot = (ImageView) findViewById(R.id.img_main_overlay_boot);
        findViewById(R.id.menu).setOnClickListener(this);
        init();
    }

    private void init() {
        if (this.mSimSlotUtil.isSim1Enabled()) {
            this.txt_sim1.setText("SIM1 : OK");
        }
        if (this.mSimSlotUtil.isSim2Enabled()) {
            this.txt_sim2.setText("SIM2 : OK");
        }
        try {
            this.version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.i(TAG, "init: " + this.version);
            Log.i(TAG, "init: " + this.settings.getString("Version", null));
        } catch (NameNotFoundException e) {
        }
        if (this.settings.getString("Version", null) == null) {
            updateDialog(true);
        } else if (Float.valueOf(this.version).floatValue() > Float.valueOf(this.settings.getString("Version", null)).floatValue()) {
            updateDialog(true);
        }
    }

    private void updateDialog(boolean firstrun) {
        AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
        updateDialog.setTitle("FIXX " + this.version + " Changelog").setMessage((CharSequence) "오버레이 기능 추가\n디자인 변경 (네비게이션바 포함)\n실행중인 서비스를 사용자가 종료할 수 있도록 변경\n메모리를 정리했을 때 서비스 종료되는 문제 수정\n전호를 수신했음에도 부재중전화로 뜨는 문제 해결").setPositiveButton((int) R.string.OK, new C01921());
        if (firstrun) {
            updateDialog.setNegativeButton(getString(R.string.Nop), new C01932()).show();
        } else {
            updateDialog.show();
        }
    }

    protected void onResume() {
        super.onResume();
        this.ErrorLogSize = this.mCallLogUtil.getErrorLogSize();
        if (this.ErrorLogSize != 0) {
            if (this.ErrorLogSize < 10) {
                this.txt_count.setText("0" + String.valueOf(this.ErrorLogSize));
            } else {
                this.txt_count.setText(String.valueOf(this.ErrorLogSize));
            }
            this.txt_count_ea.setVisibility(0);
            this.txt_exist.setVisibility(0);
        } else {
            this.txt_count.setTextSize(60.0f);
            this.txt_count.setText("없습니다 :D");
            this.txt_count_ea.setVisibility(4);
            this.txt_exist.setVisibility(4);
        }
        if (this.mUtil.isServiceRunningCheck("AutoFixService")) {
            this.img_autofix.setImageResource(R.drawable.ic_check);
        } else {
            this.img_autofix.setImageResource(R.drawable.ic_clear);
        }
        if (this.mUtil.isServiceRunningCheck("OverlayService")) {
            this.img_overlay.setImageResource(R.drawable.ic_check);
        } else {
            this.img_overlay.setImageResource(R.drawable.ic_clear);
        }
        this.autoFixBoot = Boolean.valueOf(this.settings.getBoolean("autoFixBoot", false));
        if (this.autoFixBoot.booleanValue()) {
            this.img_autofix_boot.setVisibility(4);
        } else {
            this.img_autofix_boot.setVisibility(0);
        }
        this.OverlayBoot = Boolean.valueOf(this.settings.getBoolean("overlayBoot", false));
        if (this.OverlayBoot.booleanValue()) {
            this.img_overlay_boot.setVisibility(4);
        } else {
            this.img_overlay_boot.setVisibility(0);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
//                showPopup(view);
                return;
            default:
                return;
        }
    }

//    private void showPopup(View v) {
//        PopupMenu popupMenu = new PopupMenu(this, v);
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.inflate(R.menu.menu);
//        popupMenu.show();
//    }
//
//    public boolean onMenuItemClick(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.update:
//                updateDialog(false);
//                break;
//            case R.id.about:
//                startActivity(new Intent(this, EasterEgg.class));
//                break;
//        }
//        return false;
//    }
}
