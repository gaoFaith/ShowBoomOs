package com.showboom.showboomlauncher.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.showboom.showboomlauncher.R;

/**
 * 主屏webView
 * Created by gaopeng on 2018/5/15.
 */

public class HomeWebView extends LinearLayout {
    private Context mContext;
    private WebView homeWebView;
    private String webUrl;

    public HomeWebView(Context context) {
        super(context);
    }

    public HomeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setContext(Context context, String url) {
        this.mContext = context;
        this.webUrl = url;
        initView();
        initDate();
    }

    private void initView() {
        homeWebView = findViewById(R.id.home_web_view_widget);

    }

    private void initDate() {
        WebSettings settings = homeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setTextZoom(100);
        homeWebView.loadUrl(webUrl.trim());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (homeWebView.canGoBack()) {
                homeWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
