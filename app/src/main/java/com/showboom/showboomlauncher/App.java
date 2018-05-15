package com.showboom.showboomlauncher;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.showboom.showboomlauncher.utils.HmSharedPreferencesUtils;
import com.showboom.showboomlauncher.utils.NetUtils;
import com.showboom.showboomlauncher.utils.T;

/**
 * Created by gaopeng on 2018/5/7.
 */

public class App extends Application {
    public static final String TAG = "ShowBoom";
    public static Context context;
    //屏幕宽和高
    public static int H,W;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        HmSharedPreferencesUtils.getInstance(context);
        //JPushInterface.setDebugMode(true);
        //JPushInterface.init(context);
        T.register(this);
        NetUtils.register(this);
    }
    public void getScreen(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        H=dm.heightPixels;
        W=dm.widthPixels;
    }
}
