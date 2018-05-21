package com.showboom.showboomlauncher.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.showboom.showboomlauncher.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gaopeng.
 */
public class HttpUtils {
    //http连接和读取时长
    private static final int TIMEOUT_IN_MILLIONS = 3000;
    //请求结果
//     String result = "";

    public static void doPostAsyn(final Handler handler, final int what, final String url, final String params) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(App.TAG, "扫码参数=" + params);
                    String result = doPost(url, params);
                    handler.sendMessage(Message.obtain(handler, what, result));
                } catch (Exception e) {
                    if (handler != null) {
                        handler.sendMessage(Message.obtain(handler, what, ""));
                    }
                    Log.e(App.TAG, "扫码_error=" + e.getMessage());
                }
            }
        }.start();
    }

    public static String doPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String data = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            if (param != null && !param.trim().equals("")) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                data += line;
            }
            //result = data;
        } catch (Exception e) {
            Log.d(App.TAG, "post_error=" + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return data;
    }
}