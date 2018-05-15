package com.showboom.showboomlauncher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by gaopeng on 2018/5/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        context = this;
        initUIAndListener();
        initData();
    }

    /**
     * 设置layout
     */
    public abstract int initContentView();

    /**
     * 初始化UI和Listener
     */
    protected abstract void initUIAndListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * Toast工具类
     */
    public Toast toast;
    public void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public void showToast(BaseActivity activity, String text) {
        if (toast == null) {
            toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public void showToast(int textId) {
        showToast(getString(textId));
    }

    public BaseActivity getActivity() {
        return this;
    }

    public Intent getIntent(Class clazz) {
        return new Intent(context, clazz);
    }

    public void startIntent(Class clazz) {
        startActivity(getIntent(clazz));
    }
}
