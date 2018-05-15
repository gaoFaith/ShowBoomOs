package com.showboom.showboomlauncher.widget;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.showboom.showboomlauncher.App;
import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.activity.WebViewActivity;
import com.showboom.showboomlauncher.listener.NoDoubleClickListener;
import com.showboom.showboomlauncher.loader.GlideImageLoader;
import com.showboom.showboomlauncher.utils.T;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gaopeng on 2018/5/8.
 * 主屏UI
 */

public class HomeView extends RelativeLayout implements OnBannerListener, MyScrollView.OnScrollChangeListener, View.OnClickListener {
    public static final String HM_WIFI_AP_STATE_CHANGE_ACTION = "com.heimilink.HmService.WIFI_AP_STATE_CHANGED";
    public static final String WIFI_AP_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final String HomeActivity = "com.heimilink.showboom.activity.HomeActivity";
    public static final String WiFiActivity = "com.heimilink.showboom.activity.SetWifiActivity";
    public static List<?> images = new ArrayList<>();
    private Context context;
    private Banner banner;
    private Toolbar toolbar;
    private MyScrollView scrollView;
    private TextView hotelName, userName, userLieaveTime;
    private LinearLayout userCenterLayout, recommendLayout, homeServiceOne, homeServiceTwo, homeServiceThree, homeServiceFour;
    //精选app和商城布局
    private RelativeLayout shopOneLayout;
    private LinearLayout shopTwoLayout, shopThreeLayout, shopFourLayout, shopFiveLayout, appOneLayout, appTwoLayout, appThreeLayout;
    //顶部消息和Wi-Fi图标
    private ImageView msgImg, wifiImg;
    private HomeReceiver homeReceiver;
    private WifiManager wifiManager;
    private static boolean isApOpend;
    private HomeRecommendListener listener;

    public HomeView(Context context) {
        super(context);
    }

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setContext(Context context, HomeRecommendListener listener) {
        this.context = context;
        this.listener = listener;
        initView();
        initData();
        registerReceiver();
    }

    private void initView() {
        toolbar = findViewById(R.id.home_toolbar);
        scrollView = findViewById(R.id.home_scroll);
        banner = findViewById(R.id.home_ad_banner);
        hotelName = findViewById(R.id.home_hotel_info);
        msgImg = findViewById(R.id.msg_Img);
        wifiImg = findViewById(R.id.wifi_img);
        userName = findViewById(R.id.user_name);
        userLieaveTime = findViewById(R.id.user_leave_time);
        userCenterLayout = findViewById(R.id.user_center_layout);
        recommendLayout = findViewById(R.id.home_recommend_layout);//底部推荐布局

        homeServiceOne = findViewById(R.id.home_service_one);//酒店服务1
        homeServiceTwo = findViewById(R.id.home_service_two);//酒店服务2
        homeServiceThree = findViewById(R.id.home_service_three);//酒店服务3
        homeServiceFour = findViewById(R.id.home_service_four);//酒店服务4

        shopOneLayout = findViewById(R.id.youzan_shop_one);
        shopTwoLayout = findViewById(R.id.youzan_shop_two);
        shopThreeLayout = findViewById(R.id.youzan_shop_three);
        shopFourLayout = findViewById(R.id.youzan_shop_four);
        shopFiveLayout = findViewById(R.id.youzan_shop_five);
        appOneLayout = findViewById(R.id.youzan_app_one);
        appTwoLayout = findViewById(R.id.youzan_app_two);
        appThreeLayout = findViewById(R.id.youzan_app_three);


        recommendLayout.setOnClickListener(this);
        homeServiceOne.setOnClickListener(this);
        homeServiceTwo.setOnClickListener(this);
        homeServiceThree.setOnClickListener(this);
        homeServiceFour.setOnClickListener(this);

        shopOneLayout.setOnClickListener(this);
        shopTwoLayout.setOnClickListener(this);
        shopThreeLayout.setOnClickListener(this);
        shopFourLayout.setOnClickListener(this);
        shopFiveLayout.setOnClickListener(this);

        appOneLayout.setOnClickListener(this);
        appTwoLayout.setOnClickListener(this);
        appThreeLayout.setOnClickListener(this);

        images = Arrays.asList(getResources().getStringArray(R.array.url));
        Log.d(App.TAG, "HomeView init");
        //本地图片数据（资源文件）
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.banner_default_bg);

        banner.setImages(list).setImageLoader(new GlideImageLoader());
        banner.setViewPagerIsScroll(false);//是否允许手滑动
        banner.setDelayTime(5000);
        banner.isAutoPlay(true);
        banner.setOnBannerListener(this);
        banner.start();
        scrollView.setOnScrollChangeListener(this);

    }

    private void initData() {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        isApOpend = getWifiAPState();
        wifiImg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                startShowBoomApp(WiFiActivity);
            }
        });
        msgImg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                startShowBoomApp(HomeActivity);
            }
        });
        userCenterLayout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                startShowBoomApp(HomeActivity);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_recommend_layout:
                listener.showRecommendLayout();
                break;
            case R.id.home_service_one:

                break;
            case R.id.home_service_two:

                break;
            case R.id.home_service_three:

                break;
            case R.id.home_service_four:
                startShowBoomApp(WiFiActivity);
                break;
            case R.id.youzan_shop_one:

                break;
            case R.id.youzan_shop_two:

                break;
            case R.id.youzan_shop_three:

                break;
            case R.id.youzan_shop_four:

                break;
            case R.id.youzan_shop_five:

                break;
            case R.id.youzan_app_one:

                break;
            case R.id.youzan_app_two:

                break;
            case R.id.youzan_app_three:

                break;
        }
    }

    private void startShowBoomApp(String activityStr) {
        if (activityStr == null || activityStr.isEmpty()) return;
        ComponentName cm = new ComponentName("com.heimilink.showboom", activityStr);
        Intent intent = new Intent();
        intent.setComponent(cm);
        context.startActivity(intent);
    }

    private void startAppByPackageName(String pag) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pag);
            context.startActivity(intent);
        } catch (Exception e) {
            T.showShort("未安装该应用");
            e.printStackTrace();
        }
    }

    private void registerReceiver() {
        homeReceiver = new HomeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(HM_WIFI_AP_STATE_CHANGE_ACTION);
        filter.addAction(WIFI_AP_STATE_CHANGE_ACTION);
        context.registerReceiver(homeReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (homeReceiver != null) {
            context.unregisterReceiver(homeReceiver);
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Log.d(App.TAG, "OnBannerClick" + position);
        startWebActivity("https://www.baidu.com");
    }

    private void startWebActivity(String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("home_web_url", url);
        context.startActivity(intent);
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int l, int t, int oldl, int oldt) {
        float heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
        float scrollY = scrollView.getScrollY();//该值 大于0
//        float alpha = 1 - scrollY / (heightPixels / 3);// 0~1 透明度是1~0
        float alpha = scrollY / (heightPixels / 3);// 0~1 透明度是1~0
        toolbar.setAlpha(alpha);
        if (alpha <= 0.2) {
            toolbar.setAlpha(1);
            toolbar.setBackgroundResource(R.mipmap.toobar_bg);
            msgImg.setSelected(false);
            msgImg.setBackgroundResource(R.mipmap.icon_bg);
            wifiImg.setBackgroundResource(R.mipmap.icon_bg);
            if (!isApOpend) {
                wifiImg.setSelected(false);
            }
        } else if (alpha >= 0.8f) {
            toolbar.setBackgroundResource(R.color.youzan_white_bg);
            msgImg.setSelected(true);
            if (!isApOpend) {
                wifiImg.setSelected(true);
            }
            msgImg.setBackgroundResource(R.color.transparency);
            wifiImg.setBackgroundResource(R.color.transparency);
            hotelName.setTextColor(getResources().getColor(R.color.room_item_title));
        } else {
            hotelName.setTextColor(getResources().getColor(R.color.youzan_white_bg));
            toolbar.setBackgroundResource(R.mipmap.toobar_bg);
        }
        invalidate();
        // Log.d(App.TAG, "alpha=" + alpha + ";scrollY=" + scrollY + ";heightPixels=" + heightPixels);
    }

    private boolean getWifiAPState() {
        boolean isOpen = false;
        try {
            Method method2 = wifiManager.getClass().getMethod("isWifiApEnabled");
            isOpen = (Boolean) method2.invoke(wifiManager);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return isOpen;
    }

    public interface HomeRecommendListener {
        void showRecommendLayout();
    }

    private class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) return;
            isApOpend = getWifiAPState();
            if (isApOpend) {
                wifiImg.setImageResource(R.mipmap.wifi_open);
            } else {
                wifiImg.setImageResource(R.drawable.home_wifi_bg);
            }
        }
    }

}