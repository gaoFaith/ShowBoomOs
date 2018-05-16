package com.showboom.showboomlauncher.adapter;


public class AppListAdvert {
    private int id;
    private int openType;
    private String desc;
    private String imageUrl;
    private String openTarget;

    public AppListAdvert(int id, String imageUrl, String openTarget) {
        this(id, AppListAdapter.Advert_Open_Link, "", imageUrl, openTarget);
    }
    public AppListAdvert(int id, int openType, String imageUrl, String openTarget) {
        this(id, openType, "", imageUrl, openTarget);
    }

    public AppListAdvert(int id, int openType, String desc, String imageUrl, String openTarget) {
        this.id = id;
        this.openType = openType;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.openTarget = openTarget;
    }

    public int getId() {
        return id;
    }

    public int getOpenType() {
        return openType;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOpenTarget() {
        return openTarget;
    }
}
