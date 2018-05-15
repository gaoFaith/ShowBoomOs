package com.showboom.showboomlauncher.api;


import com.showboom.showboomlauncher.bean.HttpResult;

import io.reactivex.functions.Function;


/**
 * Created by gaopeng on 2018/3/20.
 */

public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(HttpResult<T> tHttpResult) throws Exception {
        if (tHttpResult.code != 0) {
            if (tHttpResult.message != null && !tHttpResult.message.isEmpty()) {
                com.showboom.showboomlauncher.utils.T.showShort(tHttpResult.message);
            }
            return null;
        }

        return tHttpResult.data;
    }
}