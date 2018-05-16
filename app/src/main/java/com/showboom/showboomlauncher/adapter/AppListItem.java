package com.showboom.showboomlauncher.adapter;

public class AppListItem {
    private int type;
    private AppListApp app;
    private AppListAdvert advert1;
    private AppListAdvert advert2;

    public AppListItem(int type, AppListApp app, AppListAdvert advert1, AppListAdvert advert2) {
        this.type = type;
        this.app = app;
        this.advert1 = advert1;
        this.advert2 = advert2;
    }

    public int getType() {
        return type;
    }

    public AppListApp getApp() {
        return app;
    }

    public AppListAdvert getAdvert1() {
        return advert1;
    }

    public AppListAdvert getAdvert2() {
        return advert2;
    }
}
