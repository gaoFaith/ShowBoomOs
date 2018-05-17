package com.showboom.showboomlauncher.widget;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.adapter.AppListAdapter;
import com.showboom.showboomlauncher.adapter.AppListAdvert;
import com.showboom.showboomlauncher.adapter.AppListApp;
import com.showboom.showboomlauncher.adapter.AppListItem;

import java.util.Arrays;
import java.util.List;


public class AppStaticView extends RelativeLayout {
    private static final String TAG = "AppStaticView";

    private Context mContext;
    private UnScrollListView appListScrollView;
    private AppListAdapter appListAdapter;

    private static List<AppListItem> testAppList = Arrays.asList(
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(1, "com.autonavi.minimap", "http://imtt.dd.qq.com/16891/C94BB83942679667EF8E15ECCA7B5FA8.apk?fsname=com.autonavi.minimap_8.50.0.2169_6500.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_7678_1525245885/96",
                            "高德地图", "中国专业的手机地图，超过7亿用户正在使用！", "3000万人好评", "68.29MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(2, "com.mfw.voiceguide", "http://gdown.baidu.com/data/wisegame/f59af74d0a3ef730/lvxingfanyiguan_95.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_10265_1439195638/96",
                            "旅行翻译官", "让您的设备开口说话，旅行不再愁", "300万人好评", "8.96MB"), null, null),
            new AppListItem(AppListAdapter.Type_Advert,
                    null, new AppListAdvert(15, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1525939853620&di=b5a4f6a178a48c8711c5713520109132&imgtype=0&src=http%3A%2F%2Fimg.wanyx.com%2FUploads%2Fueditor%2Fimage%2F20170407%2F1491558056524649.jpg","http://www.baidu.com"),
                        new AppListAdvert(16, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1525940097760&di=73214445386a9b00b4c1a2151e6c4ba8&imgtype=0&src=http%3A%2F%2Fi2.17173cdn.com%2F2fhnvk%2FYWxqaGBf%2Fcms3%2FvLytDRblbjclAmv.jpg","https://www.sogou.com/")),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(3, "com.moji.mjweather", "http://imtt.dd.qq.com/16891/7D23C31900EE6F4166F8E1787EFD6578.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_9206_1526024837/96",
                            "墨迹天气", "全球约5亿人在使用的天气APP", "1亿人好评", "15.26MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(4, "com.flightmanager.view", "http://imtt.dd.qq.com/16891/13B020A1EB4888FA8EAF900115419904.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_6789_1526224413/96",
                            "航班管家", "直销国际机票，航班延误提醒", "1000万人好评", "54.73MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(5, "com.qyer.android.jinnang", "http://imtt.dd.qq.com/16891/A6202E5CFC9D5179A1CC58F6A5CCA396.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_276911_1524796670/96",
                            "穷游", "最强出境游神器来袭！出境游尽在穷游！", "100万人好评", "32.77MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(6, "com.taobao.trip", "http://imtt.dd.qq.com/16891/A42475B3C4742DC959A998A75B333A9F.apk",
                                    "http://pp.myapp.com/ma_icon/0/icon_11188_1526356277/96",
                                    "飞猪", "阿里巴巴旗下的旅行应用", "900万人好评", "56MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(7, "com.example.rentalcarapp", "http://imtt.dd.qq.com/16891/1387492865438283B6534E70B85D7D2A.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_12065844_1525684435/96",
                            "首汽租车", "专业的综合性汽车租赁服务商", "80万人好评", "24.69MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(8, "com.ss.android.article.news", "http://imtt.dd.qq.com/16891/9D108D15F602D8D28F79D43F64205888.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_213141_1525938149/96",
                            "今日头条", "专业的综合性汽车租赁服务商", "9000万人好评", "22.02MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(9, "com.tratao.xcurrency", "http://imtt.dd.qq.com/16891/6B13428329E35163B43944D74EAA931F.apk",
                            "http://pp.myapp.com/ma_icon/0/icon_11833996_1523527082/96",
                            "极简汇率", "全球首款采用 Material Design 的汇率换算应用", "50万人好评", "7.05MB"), null, null),
            new AppListItem(AppListAdapter.Type_App,
                    new AppListApp(10, "com.psyone.brainmusic", "http://imtt.dd.qq.com/16891/1FE1CDF97A3A21E7D39860E290093610.apk",
                            "http://d.hiphotos.bdimg.com/wisegame/wh%3D72%2C72/sign=9e890f619cef76c6d087f32caf3acac8/a044ad345982b2b7abf6e4843dadcbef76099b01.jpg",
                            "小睡眠", "一款出类拔萃的睡眠辅助新概念产品", "40万人好评", "22.66MB"), null, null)
            );

    public AppStaticView(Context context) {
        super(context);
        mContext = context;
    }

    public AppStaticView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Log.d(TAG, "AppStaticView Constructor");
        LayoutInflater.from(context).inflate(R.layout.app_static_view_layout, this);
        appListScrollView = findViewById(R.id.app_list);
        appListAdapter = new AppListAdapter(context, testAppList);
        appListScrollView.setAdapter(appListAdapter);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow");

        IntentFilter filterInstall = new IntentFilter();
        filterInstall.addAction(Intent.ACTION_PACKAGE_ADDED);
        filterInstall.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filterInstall.addDataScheme("package");
        mContext.registerReceiver(mInstallReceiver, filterInstall);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        mContext.unregisterReceiver(mInstallReceiver);
        if(appListAdapter != null) {
            appListAdapter.finishTimer();
        }
    }

    private BroadcastReceiver mInstallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                if(appListAdapter != null) {
                    appListAdapter.installComplete(packageName);
                }

            } else if(Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                if(appListAdapter != null) {
                    appListAdapter.removeComplete(packageName);
                }
            }
        }
    };

}
