package com.showboom.showboomlauncher.bean;

import java.io.Serializable;

/**
 * Created by gaopeng on 2018/5/7.
 */

public class ScreenInfo implements Serializable {
    int layoutId;
    String tag;
    DataBean data;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    private static class DataBean {

    }
}
