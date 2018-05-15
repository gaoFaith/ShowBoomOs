package com.showboom.showboomlauncher.api;

/**
 * Created by gaopeng on 2018/3/20.
 */

public class ApiException extends RuntimeException {

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

}
