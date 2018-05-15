package com.showboom.showboomlauncher.activity;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.showboom.showboomlauncher.App;
import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.widget.WebViewLoadingDialog;

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private Dialog loadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initUIAndListener() {
        webView = findViewById(R.id.home_web_view);
        loadingDialog = WebViewLoadingDialog.createLoadingDialog(WebViewActivity.this, "");
    }

    @Override
    protected void initData() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setTextZoom(100);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 80) {
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                        loadingDialog = null;
                    }
                } else {
                    if (loadingDialog == null) {
                        loadingDialog = WebViewLoadingDialog.createLoadingDialog(WebViewActivity.this, "");
                    }
                    loadingDialog.show();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        loadUrl();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadUrl() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("home_web_url");
        if (url != null && url.isEmpty()) return;
        Log.d(App.TAG, "home_web_url=" + url);
        webView.loadUrl(url);
    }
}
