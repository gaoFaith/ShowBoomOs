package com.showboom.showboomlauncher.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.showboom.showboomlauncher.App;
import com.showboom.showboomlauncher.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaopeng on 2018/3/20.
 */

public class StringUtil {

    // 判断字符串的合法性
    public static boolean checkStr(String str) {
        if (null == str) {
            return false;
        }
        if ("".equals(str)) {
            return false;
        }
        if ("".equals(str.trim())) {
            return false;
        }
        if ("null".equals(str)) {
            return false;
        }
        return true;
    }


    // 判断密码格式是否正确 6-13位
    public static boolean isPassword(String password) {
        return password.length() >= 6 || password.length() <= 13;
    }


    /**
     * 验证字符串是否为空
     */
    public static boolean empty(String param) {
        return param == null || param.trim().length() < 1;
    }


    /**
     * 验证手机号码
     */
    public static boolean isPhone(String phone) {
        if (!checkStr(phone)) {
            return false;
        }
        String pattern = "^1[3|4|5|7|8][0-9]{9}$";
        return phone.matches(pattern);
    }

    /**
     * 判断是否是身份证
     */

    public static boolean isIDCardNum(String IDCards) {

        Pattern p = Pattern
                .compile("((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" +
                        "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" +
                        "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))");
        Matcher m = p.matcher(IDCards);
        return m.matches();
    }

    /**
     * 获取手机Imei
     */
    public static String getPhoneImei() {
        TelephonyManager telManager = (TelephonyManager) App.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telManager != null) {
            String imei = telManager.getDeviceId();
            return imei;
        }
        return "";
    }

    public static String getStoken() {
        String sto = (String) HmSharedPreferencesUtils.getParam(Constants.STOKEN, "");
        if (sto != null && !sto.isEmpty()) {
            return sto;
        }
        return "";
    }
    public static String getUserId() {
        String uid = (String) HmSharedPreferencesUtils.getParam(Constants.USER_ID, "");
        if (uid != null && !uid.isEmpty()) {
            return uid;
        }
        return "";
    }

    public static String getAppVersion() {
        String versionName = "";
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = App.context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(App.context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e(App.TAG, "getAppVersion_Exception", e);
        }
        return versionName;
    }

    public static String getStartTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return dff.format(new Date());
    }

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    public static long getStartTimeForLong() {
        String time = getStartTime();
        //Log.d(MyApplication.TAG, "time=" + time);
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Date date = null;
        String times = null;
        try {
            date = dff.parse(time);
            String longTime = String.valueOf(date.getTime());
            times = longTime.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(MyApplication.TAG, "time=" + times);
        return Long.parseLong(times);
    }
}
