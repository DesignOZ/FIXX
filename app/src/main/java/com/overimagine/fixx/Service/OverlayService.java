package com.overimagine.fixx.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.IBinder;

import androidx.annotation.Nullable;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.overimagine.fixx.R;
import com.overimagine.fixx.Util.CallLogUtil;
import com.overimagine.fixx.Util.SimSlotUtil;

import java.util.regex.Pattern;

public class OverlayService extends Service {
    private static final String TAG = "Overlay Service";
    private String Line1Number;
    private String Sim1Number;
    private String Sim2Number;
    private Editor editor;
    private boolean isMove = false;
    private boolean isMultiSimEnabled;
    private CallLogUtil mCallLogUtil;
    private WindowManager mManager;
    private LayoutParams mParams;
    private SimSlotUtil mPhoneUtil;
    private float mTouchX;
    private float mTouchY;
    private View mView;
    private OnTouchListener mViewTouchListener = new C02082();
    private int mViewX;
    private int mViewY;
    private TelephonyManager manager;
    private PhoneStateListener phoneStateListener = new myPhoneStateListener();
    private SharedPreferences settings;
    TextView txt_name;
    TextView txt_number;

    /* renamed from: com.tistory.overimagine.voltecalllogfix.ServiceRunningCheck.OverlayService$1 */
    class myPhoneStateListener extends PhoneStateListener {
        myPhoneStateListener() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            Log.i(OverlayService.TAG, "onCallStateChanged: " + incomingNumber);
            if (state == 1) {
                String number;
                if (!OverlayService.this.isMultiSimEnabled) {
                    number = incomingNumber.replace(OverlayService.this.Line1Number, "").replace("&", "");
                } else if (incomingNumber.contains(OverlayService.this.Sim1Number)) {
                    number = incomingNumber.replace(OverlayService.this.Sim1Number, "").replace("&", "");
                } else {
                    number = incomingNumber.replace(OverlayService.this.Sim2Number, "").replace("&", "");
                }
                OverlayService.this.txt_number.setText(OverlayService.this.PhoneNumberPattern(number));
                OverlayService.this.txt_name.setText(OverlayService.this.mCallLogUtil.getContactName(number));
                OverlayService.this.mView.setVisibility(View.VISIBLE);
            }
            if (state == 0 || state == 2) {
                OverlayService.this.mView.setVisibility(View.GONE);
            }
        }
    }

    /* renamed from: com.tistory.overimagine.voltecalllogfix.ServiceRunningCheck.OverlayService$2 */
    class C02082 implements OnTouchListener {
        C02082() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    OverlayService.this.isMove = false;
                    OverlayService.this.mTouchX = event.getRawX();
                    OverlayService.this.mTouchY = event.getRawY();
                    OverlayService.this.mViewX = OverlayService.this.mParams.x;
                    OverlayService.this.mViewY = OverlayService.this.mParams.y;
                    break;
                case 1:
                    if (!OverlayService.this.isMove) {
                        Toast.makeText(OverlayService.this.getApplicationContext(), "터치됨", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    OverlayService.this.editor.putInt("xParam", OverlayService.this.mParams.x);
                    OverlayService.this.editor.putInt("yParam", OverlayService.this.mParams.y);
                    Log.i(OverlayService.TAG, "mParams.x: " + OverlayService.this.mParams.x);
                    Log.i(OverlayService.TAG, "mParams.y: " + OverlayService.this.mParams.y);
                    OverlayService.this.editor.commit();
                    break;
                case 2:
                    OverlayService.this.isMove = true;
                    int x = (int) (event.getRawX() - OverlayService.this.mTouchX);
                    int y = (int) (event.getRawY() - OverlayService.this.mTouchY);
                    if (x > -5 && x < 5 && y > -5 && y < 5) {
                        OverlayService.this.isMove = false;
                        break;
                    }
                    OverlayService.this.mParams.x = OverlayService.this.mViewX + x;
                    OverlayService.this.mParams.y = OverlayService.this.mViewY + y;
                    OverlayService.this.mManager.updateViewLayout(OverlayService.this.mView, OverlayService.this.mParams);
                    break;
            }
            return true;
        }
    }

    public void onCreate() {
        super.onCreate();
        this.manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        this.manager.listen(this.phoneStateListener, 32);
        this.mPhoneUtil = new SimSlotUtil(getBaseContext());
        this.mCallLogUtil = new CallLogUtil(getBaseContext());
        this.isMultiSimEnabled = this.mPhoneUtil.isMultiSimEnabled();
        this.Line1Number = this.mPhoneUtil.getLine1Number();
        this.Sim1Number = this.mPhoneUtil.getSim1MSISDN();
        this.Sim2Number = this.mPhoneUtil.getSim2MSISDN();
        this.settings = getBaseContext().getSharedPreferences("settings", 0);
        this.editor = this.settings.edit();
        this.mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        this.mView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.service_overlay, null);
        this.mView.setVisibility(View.GONE);
        this.mView.setOnTouchListener(this.mViewTouchListener);
        this.mParams = new LayoutParams(-2, -2, 2006, 8, -3);
        this.mParams.gravity = 17;
        Display display = this.mManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        this.mParams.x = 0;
        this.mParams.y = -160;
        this.txt_name = (TextView) this.mView.findViewById(R.id.txt_overlay_name);
        this.txt_number = (TextView) this.mView.findViewById(R.id.txt_overlay_number);
        this.mManager.addView(this.mView, this.mParams);
    }

    private String PhoneNumberPattern(String number) {
        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
        if (Pattern.matches(regEx, number)) {
            return number.replaceAll(regEx, "$1-$2-$3");
        }
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mView != null) {
            this.mManager.removeView(this.mView);
            this.mView = null;
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }
}
