package com.showboom.showboomlauncher.green_dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Launcher 数据类
 * Created by gaopeng on 2018/5/17.
 */
@Entity
public class LauncherInfo {
    @Id(autoincrement = true)
    private Long id;
    private Long time;
    private String version;
    private String page_minus_one;
    private String page_minus_two;
    private String page_zero;
    private String page_one;
    private String page_two;
    private String page_three;
    private String page_four;
    private String page_five;
    private String page_six;
    private String page_seven;
    private String page_eight;
    private String page_nine;
    private String page_ten;
    @Generated(hash = 1939506760)
    public LauncherInfo(Long id, Long time, String version, String page_minus_one,
            String page_minus_two, String page_zero, String page_one,
            String page_two, String page_three, String page_four, String page_five,
            String page_six, String page_seven, String page_eight, String page_nine,
            String page_ten) {
        this.id = id;
        this.time = time;
        this.version = version;
        this.page_minus_one = page_minus_one;
        this.page_minus_two = page_minus_two;
        this.page_zero = page_zero;
        this.page_one = page_one;
        this.page_two = page_two;
        this.page_three = page_three;
        this.page_four = page_four;
        this.page_five = page_five;
        this.page_six = page_six;
        this.page_seven = page_seven;
        this.page_eight = page_eight;
        this.page_nine = page_nine;
        this.page_ten = page_ten;
    }
    @Generated(hash = 1214725968)
    public LauncherInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getPage_minus_one() {
        return this.page_minus_one;
    }
    public void setPage_minus_one(String page_minus_one) {
        this.page_minus_one = page_minus_one;
    }
    public String getPage_minus_two() {
        return this.page_minus_two;
    }
    public void setPage_minus_two(String page_minus_two) {
        this.page_minus_two = page_minus_two;
    }
    public String getPage_zero() {
        return this.page_zero;
    }
    public void setPage_zero(String page_zero) {
        this.page_zero = page_zero;
    }
    public String getPage_one() {
        return this.page_one;
    }
    public void setPage_one(String page_one) {
        this.page_one = page_one;
    }
    public String getPage_two() {
        return this.page_two;
    }
    public void setPage_two(String page_two) {
        this.page_two = page_two;
    }
    public String getPage_three() {
        return this.page_three;
    }
    public void setPage_three(String page_three) {
        this.page_three = page_three;
    }
    public String getPage_four() {
        return this.page_four;
    }
    public void setPage_four(String page_four) {
        this.page_four = page_four;
    }
    public String getPage_five() {
        return this.page_five;
    }
    public void setPage_five(String page_five) {
        this.page_five = page_five;
    }
    public String getPage_six() {
        return this.page_six;
    }
    public void setPage_six(String page_six) {
        this.page_six = page_six;
    }
    public String getPage_seven() {
        return this.page_seven;
    }
    public void setPage_seven(String page_seven) {
        this.page_seven = page_seven;
    }
    public String getPage_eight() {
        return this.page_eight;
    }
    public void setPage_eight(String page_eight) {
        this.page_eight = page_eight;
    }
    public String getPage_nine() {
        return this.page_nine;
    }
    public void setPage_nine(String page_nine) {
        this.page_nine = page_nine;
    }
    public String getPage_ten() {
        return this.page_ten;
    }
    public void setPage_ten(String page_ten) {
        this.page_ten = page_ten;
    }
}
