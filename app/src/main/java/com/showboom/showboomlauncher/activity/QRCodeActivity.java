package com.showboom.showboomlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.showboom.showboomlauncher.Constants;
import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.listener.HomeListener;
import com.showboom.showboomlauncher.service.HmPollingService;
import com.showboom.showboomlauncher.utils.StringUtil;
import com.showboom.showboomlauncher.widget.LetterSpacingTextView;

public class QRCodeActivity extends BaseActivity {
    public static final String LOGIN_STATE_ACTION = "com.heimilink.login_action";
    public static final int OPEN_SERVICE = 1;
    public static final int CLOSE_SERVICE = 2;
    private LetterSpacingTextView titleText;
    private ImageView qrImg;
    private String qrContent;
    private PollingReceiver mReceiver;
    private HomeListener homeListener;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HmPollingService.SUCCESSFUl:
                    handleService(CLOSE_SERVICE);
                    finish();
                    startIntent(QRCodeActivity.class);
                    break;
                case HmPollingService.TIME_OUT:
                    //超时退出应用
                    //handleService(CLOSE_SERVICE);
                    //finish();
                    //System.exit(0);
                    break;
            }
        }
    };

    @Override
    public int initContentView() {
        return R.layout.activity_qrcode;
    }

    @Override
    protected void initUIAndListener() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        titleText = findViewById(R.id.hotel_qr_title);
        qrImg = findViewById(R.id.hotel_qr_img);
        titleText.setSpacing(3f);
        titleText.setText(R.string.hotel_scan_qr_hint);

        mReceiver = new PollingReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(LOGIN_STATE_ACTION);
        registerReceiver(mReceiver, filter);
        noUserData();
        homeListener = new HomeListener(this);
    }

    @Override
    protected void initData() {

    }

    private void noUserData() {
        qrContent = Constants.HM_QR_URL + StringUtil.encryptData(Constants.IMEI + StringUtil.getPhoneImei() + Constants.ICCID + StringUtil.getPhoneIccid() + Constants.VERSION + StringUtil.getProVersion());
        Bitmap bitmap = StringUtil.generateBitmap(qrContent, 422, 422);
        Bitmap hmimg = StringUtil.addLogo(bitmap, BitmapFactory.decodeResource(getResources(), R.mipmap.showboom_qr_bg));
        qrImg.setImageBitmap(hmimg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleService(OPEN_SERVICE);
        homeListener.startListen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handleService(CLOSE_SERVICE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeListener.stopListen();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleService(int flag) {
        Intent intent = new Intent(this, HmPollingService.class);
        intent.setAction(HmPollingService.ACTION);
        switch (flag) {
            case OPEN_SERVICE:
                startService(intent);
                break;
            case CLOSE_SERVICE:
                stopService(intent);
                break;
        }
    }

    class PollingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case LOGIN_STATE_ACTION:
                    int state = intent.getIntExtra("login_state", 0);
                    mHandler.sendEmptyMessage(state);
                    break;
            }
        }
    }
}
