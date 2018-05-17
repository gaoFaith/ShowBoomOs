package com.showboom.showboomlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.adapter.CustomPagerAdapter;
import com.showboom.showboomlauncher.bean.ScreenInfo;
import com.showboom.showboomlauncher.widget.HomeView;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.plugin.common.AppMgr;
import com.yuntongxun.plugin.common.ClientUser;
import com.yuntongxun.plugin.common.SDKCoreHelper;
import com.yuntongxun.plugin.common.common.utils.ECPreferences;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.RongXInUtils;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;

import java.util.Arrays;

public class HomeActivity extends BaseActivity implements HomeView.HomeRecommendListener {
    private ViewPager fragmentViewPager;
    private CustomPagerAdapter pagerAdapter;
    private FragmentManager fragmentManager;
    private String[] tags = {"0", "1", "2", "4"};

    private String im_account;
    private InternalReceiver internalReceiver = new InternalReceiver();

    @Override
    public int initContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void initUIAndListener() {
        fragmentViewPager = findViewById(R.id.home_view_pager);

        fragmentManager = getSupportFragmentManager();
        ScreenInfo info = new ScreenInfo();
        info.setLayoutId(R.layout.home_content_layout);
        info.setTag("1");

        pagerAdapter = new CustomPagerAdapter(fragmentManager, info);

        pagerAdapter.addDatas(Arrays.asList(tags));
        fragmentViewPager.setAdapter(pagerAdapter);
        fragmentViewPager.setCurrentItem(1);
        //fragmentViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void initData() {
        TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        if(imei == null || imei.equals("")) {
            im_account = null;
        } else {
            im_account = imei;
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(RongXInUtils.ACTION_KICK_OFF);
        filter.addAction(SDKCoreHelper.ACTION_LOGOUT);
        registerReceiver(internalReceiver, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SDKCoreHelper.ACTION_SDK_CONNECT);
        intentFilter.addAction(SDKCoreHelper.ACTION_LOGOUT);
        registerReceiver(mSDKNotifyReceiver, intentFilter);

        if (AppMgr.getClientUser() != null) {
            LogUtil.d("SDK auto connect...");
            SDKCoreHelper.init(getApplicationContext());
        } else {
            if(im_account != null) {
                ClientUser.UserBuilder builder = new ClientUser.UserBuilder(im_account, "顾客");
                builder.setAppKey("8aaf070862dcc47f0162f13b517a09b7");// AppId
                builder.setAppToken("41dc8ccee15200b41f5d0f52ed0234f7");// AppToken
                SDKCoreHelper.login(builder.build());
                LogUtil.d("SDK login");
            }
        }
    }

    private BroadcastReceiver mSDKNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SDKCoreHelper.isLoginSuccess(intent)) {
                String pushToken = ECPreferences.getSharedPreferences().getString("pushToken", null);
                LogUtil.d("SDK connect Success ,reportToken:" + pushToken);
                if (!TextUtils.isEmpty(pushToken)) {
                    ECDevice.reportHuaWeiToken(pushToken);
                }
                //btn_voip_voice.setEnabled(true);
            } else {
                int error = intent.getIntExtra("error", 0);
                if (error == SdkErrorCode.CONNECTING) return;
                LogUtil.e("登入失败[" + error + "]");
                //ToastUtil.showMessage("登入失败 == " + error);
            }
        }
    };

    private class InternalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals(RongXInUtils.ACTION_KICK_OFF)) {
                LogUtil.e("异地登入SDK状态" + ECDevice.isInitialized());
                if (ECDevice.isInitialized()) {
                    ECDevice.unInitial();
                }
                LogUtil.e("异地登入SDK状态new==" + ECDevice.isInitialized());
                String kickoffText = intent.getStringExtra("kickoffText");
                ToastUtil.showMessage(kickoffText);
            } else if (intent.getAction().equals(SDKCoreHelper.ACTION_LOGOUT)) {
                // 断开SDK连接之后的逻辑处理
                // ......释放资源
                if(ECDevice.isInitialized()){
                    ECDevice.unInitial();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internalReceiver);
        unregisterReceiver(mSDKNotifyReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showRecommendLayout() {
        fragmentViewPager.setCurrentItem(2,true);
    }
}
