package com.showboom.showboomlauncher.api;


import com.showboom.showboomlauncher.bean.HttpResult;
import com.showboom.showboomlauncher.bean.LauncherDataBean;
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
    @POST("api/api/index")
    Observable<HttpResult<UpdateJPushBean>> updateJpushId(@FieldMap() Map<String, Object> map);

    /**
     * 获取主屏数据
     */
    @FormUrlEncoded
    @POST("api/api/index")
    Observable<HttpResult<LauncherDataBean>> getLauncherScreenData(@FieldMap() Map<String, Object> map);
}
