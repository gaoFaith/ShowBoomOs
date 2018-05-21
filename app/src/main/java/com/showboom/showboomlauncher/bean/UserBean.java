package com.showboom.showboomlauncher.bean;

/**
 * Created by bopai on 2017/7/30.
 */
public class UserBean {
    private String stoken;
    private String phoneNum;
    private String nikeName;
    private String imgUrl;
    private String rentId;

    public String getStoken() {
        return stoken;
    }

    public void setStoken(String stoken) {
        this.stoken = stoken;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }
}