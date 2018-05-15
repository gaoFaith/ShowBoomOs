package com.showboom.showboomlauncher.net;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by gaopeng on 2018/3/20.
 */
public class ResponseInterceptor implements Interceptor {
    private String emptyString = ":\"\"";
    private String emptyObject = ":{}";
    private String emptyArray = ":[]";
    private String newChars = ":null";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String json = responseBody.string();
            MediaType contentType = responseBody.contentType();
            if (!json.contains(emptyString)) {
                ResponseBody body = ResponseBody.create(contentType, json);
                return response.newBuilder().body(body).build();
            } else {
                String replace = json.replace(emptyString, newChars);
                String replace1 = replace.replace(emptyObject, newChars);
                String replace2 = replace1.replace(emptyArray, newChars);
                ResponseBody body = ResponseBody.create(contentType, replace2);
                return response.newBuilder().body(body).build();
            }
        }
        return response;
    }
}
