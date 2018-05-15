package com.showboom.showboomlauncher.api;


import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gaopeng on 2018/3/20.
 */

public class SchedulersTransformer {
    public static <T> ObservableTransformer<T,T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }
}