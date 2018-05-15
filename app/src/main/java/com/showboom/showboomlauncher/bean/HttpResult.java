package com.showboom.showboomlauncher.bean;

/**
 * Created by gaopeng on 2018/3/20.
 */

public class HttpResult<T> {
    public int code = -1;
    public String message;
    //    public boolean result;
    public T data;
}