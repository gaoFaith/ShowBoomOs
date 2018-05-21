package com.showboom.showboomlauncher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.showboom.showboomlauncher.App;
import com.showboom.showboomlauncher.Constants;
import com.showboom.showboomlauncher.bean.UserBean;
import com.showboom.showboomlauncher.utils.HmSharedPreferencesUtils;
import com.showboom.showboomlauncher.utils.HttpUtils;
import com.showboom.showboomlauncher.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class HmPollingService extends Service {
    //开启服务的action
    public static final String ACTION = "com.heimilink.service.PollingService";
    //扫描状态的广播，包括轮询超时，成功授权登录
    public static final String LOGIN_STATE_ACTION = "com.heimilink.login_action";
    public static final String LOGIN_STATE = "login_state";
    //请求后台连接
    private final int HTTP_CODE = 1;
    //定时请求
    private final int TIMING_CODE = 2;
    //超时状态
    public static final int TIME_OUT = 3;
    //成功登录
    public static final int SUCCESSFUl = 4;
    //轮询
    public static final int SEND_HTTP = 5;
    //用户是否登录
    private int loginCode = -1;
    private Timer timer;
    //记录定时器时间，当停留5min时，关闭当前界面，回到欢迎页
    private int flag = 0;
    //请求后台用户是否扫描登录成功
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HTTP_CODE:
                    String data = (String) msg.obj;
                    parseJson(data);
                    break;
                case TIMING_CODE:
                    flag++;
                    Log.d("bopai", "loginCode=" + loginCode);
                    if (flag > 198) {
                        flag = 0;
                        sendReceiver(TIME_OUT);
                        handleService();
                        return;
                    }
                    if (loginCode == 0) {
                        sendReceiver(SUCCESSFUl);
                        loginCode = -1;
                        handleService();
                    }
                    break;
                case SEND_HTTP:
                    HttpUtils.doPostAsyn(mHandler, HTTP_CODE, App.url, Constants.SERVICE + Constants.IMEI + StringUtil.getPhoneImei() + Constants.ICCID + StringUtil.getPhoneIccid() + Constants.PRO + StringUtil.getProVersion());
                    break;
            }
        }
    };

    /**
     * 发送超时或者成功登录状态
     *
     * @param state
     */
    private void sendReceiver(int state) {
        Intent intent = new Intent();
        intent.setAction(LOGIN_STATE_ACTION);
        intent.putExtra(LOGIN_STATE, state);
        sendBroadcast(intent);
    }

    /**
     * 解析后台json数据
     *
     * @param data
     */
    private void parseJson(String data) {
        if (mHandler == null) {
            return;
        }
        if (data.isEmpty()) {
            mHandler.sendEmptyMessageDelayed(SEND_HTTP, 3000);
            return;
        }
        try {
            JSONObject object = new JSONObject(data);
            loginCode = object.getInt("code");
            boolean f = false;
            if (loginCode == 0) {
                JSONObject userInfo = new JSONObject(object.getString("data"));
                String sto = userInfo.getString("stoken");
                //String phoneNum = userInfo.getString("mobile");
                String nikeName = userInfo.getString("nickname");
                String img = userInfo.getString("header_img");
                String rentid = userInfo.getString("rentId");
                UserBean userBean = new UserBean();
                userBean.setStoken(sto);
                userBean.setImgUrl(img);
                //userBean.setPhoneNum(phoneNum);
                userBean.setNikeName(nikeName);
                userBean.setRentId(rentid);
                handleUserInfo(userBean);
            } else {
                f = mHandler.sendEmptyMessageDelayed(SEND_HTTP, 3000);
            }
            Log.d("bopai", "code=" + loginCode + ";msg=" + object.getString("message") + ";data=" + object.getString("data") + ";send_flag=" + f);
        } catch (JSONException e) {
            loginCode = -1;
            mHandler.sendEmptyMessageDelayed(SEND_HTTP, 3000);
            Log.e("bopai", "GET_HTTP_JSON_ERROR=" + e.getMessage());
        }
    }

    private void handleUserInfo(UserBean bean) {
        HmSharedPreferencesUtils.setParam(Constants.IS_LOAD, true);
        Settings.System.putString(App.context.getContentResolver(), Constants.USER_STOKEN, bean.getStoken());
        Settings.System.putString(App.context.getContentResolver(), Constants.USER_NAME, bean.getNikeName());
        //Settings.System.putString(MyApplication.context.getContentResolver(), Constant.USER_PHONE, bean.getPhoneNum());
        Settings.System.putString(App.context.getContentResolver(), Constants.USER_IMG, bean.getImgUrl());
        Settings.System.putString(App.context.getContentResolver(), Constants.USER_RENTID, bean.getRentId());
        String timeStr = String.valueOf(System.currentTimeMillis());
        Settings.System.putString(App.context.getContentResolver(), "hm_used_time", timeStr);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        httpSendTime();
        loginCode = -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleService();
    }

    /**
     * 当超时或者成功授权使用时，清理service
     */
    private void handleService() {
        if (mHandler != null) {
            mHandler.removeMessages(HTTP_CODE);
            mHandler.removeMessages(TIMING_CODE);
            mHandler = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        System.gc();
    }

    /**
     * 定时轮询
     */
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage(TIMING_CODE, loginCode);
            mHandler.sendMessage(msg);
        }
    }

    private void httpSendTime() {
        try {
            timer = new Timer();
            HttpUtils.doPostAsyn(mHandler, HTTP_CODE, App.url, Constants.SERVICE + Constants.IMEI + StringUtil.getPhoneImei() + Constants.ICCID + StringUtil.getPhoneIccid() + Constants.PRO + StringUtil.getProVersion());
            MyTimerTask task = new MyTimerTask();
            //进入界面5秒后开始请求，3秒请求一次
            timer.schedule(task, 5000, 3000);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
