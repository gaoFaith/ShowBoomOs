package com.showboom.showboomlauncher.listener;

import android.view.View;

import java.util.Calendar;

/**
 * 防止多次点击
 * Created by gaopeng on 2018/5/10.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 1500;   //点击时间间隔
    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if ((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(view);
        }
    }

    public abstract void onNoDoubleClick(View view);

}

