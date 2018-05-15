package com.showboom.showboomlauncher.api;


import com.showboom.showboomlauncher.bean.HttpResult;
import com.showboom.showboomlauncher.bean.UpdateJPushBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by gaopeng on 2018/3/20.
 */

public interface ApiInterface {


    /**
     * 上报极光ID
     */
    @FormUrlEncoded
    @POST("api/service/index")
    Observable<HttpResult<UpdateJPushBean>> updateJpushId(@FieldMap() Map<String, Object> map);
}
