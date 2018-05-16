package com.showboom.showboomlauncher.adapter;

public class AppListApp {
    private int id;
    private String pkgName;
    private String pkgUrl;
    private String iconUrl;
    private String title;
    private String desc;
    private String favorites;
    private String size;

    public AppListApp(int id, String pkgName, String pkgUrl, String iconUrl, String title, String desc, String favorites, String size) {
        this.id = id;
        this.pkgName = pkgName;
        this.pkgUrl = pkgUrl;
        this.iconUrl = iconUrl;
        this.title = title;
        this.desc = desc;
        this.favorites = favorites;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getPkgUrl() {
        return pkgUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getFavorites() {
        return favorites;
    }

    public String getSize() {
        return size;
    }
}
