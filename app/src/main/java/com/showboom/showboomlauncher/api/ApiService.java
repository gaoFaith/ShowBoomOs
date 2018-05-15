package com.showboom.showboomlauncher.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.showboom.showboomlauncher.Constants;
import com.showboom.showboomlauncher.bean.UpdateJPushBean;
import com.showboom.showboomlauncher.net.RequestInterceptor;
import com.showboom.showboomlauncher.net.ResponseInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaopeng on 2018/3/20.
 */

public class ApiService {
    private ApiInterface mApiInterface;

    private ApiService() {
        //HTTP log
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //RequestInterceptor
        RequestInterceptor requestInterceptor = new RequestInterceptor();

        //ResponseInterceptor
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();

        //OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor)
                .addInterceptor(responseInterceptor);
        if (Constants.DEBUG_MODE) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        OkHttpClient mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().setLenient().create();

        //Retrofit
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.baseUtl)
                .build();
        //ApiInterface
        mApiInterface = mRetrofit.create(ApiInterface.class);
    }

    //Singleton
    private static class SingletonHolder {
        private static final ApiService INSTANCE = new ApiService();
    }

    //Instance
    public static ApiService getApiService() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 上报极光ID事件
     */
    public void updateJpushService(Observer<UpdateJPushBean> observer, Map<String, Object> map) {
        mApiInterface.updateJpushId(map).compose(SchedulersTransformer.io_main())
                .map(new HttpResultFunc<>())
                .subscribe(observer);
    }
}