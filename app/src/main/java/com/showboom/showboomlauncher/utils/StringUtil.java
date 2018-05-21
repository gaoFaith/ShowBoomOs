package com.showboom.showboomlauncher.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.showboom.showboomlauncher.App;
import com.showboom.showboomlauncher.Constants;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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

    public static int getUrlTag() {
//        int tag = Integer.parseInt(SystemProperties.get("ro.product.lunch.type", "-1"));
        int tag = 1;
        return tag;
    }

    public static String getProID() {
        String proId = "hotel";
        return proId;
    }

    public static String getProVersion() {
        String pro = "S003";
        return pro;
    }

    public static String getPhoneIccid() {
        TelephonyManager telephonyManager = (TelephonyManager) App.context.getSystemService(Context.TELEPHONY_SERVICE);
        Cursor cursor = null;
        try {
            if (telephonyManager != null) {
                Method method = telephonyManager.getClass().getMethod("getPrimaryCard");
                int subid = (int) method.invoke(telephonyManager);
                Uri uri = Uri.parse("content://telephony/siminfo");
                cursor = App.context.getContentResolver().query(uri, new String[]{"icc_id", "sim_id"}, "sim_id=?", new String[]{String.valueOf(subid)}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex("icc_id"));
                }

            }
        } catch (Exception e) {
            Log.d(App.TAG, "getPhoneIccid_Exception=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    /**
     * 创建二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float sx = qrBitmapWidth * 1.0f / 5 / logoBitmapWidth;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

    /**
     * 二维码加密
     *
     * @param DataStr
     * @return
     */
    public static String encryptData(String DataStr) {
        if (DataStr.isEmpty()) {
            return "";
        }
        //Base64
        String Str64 = Base64.encodeToString(DataStr.getBytes(), Base64.NO_WRAP);
        Random random = new Random();
        //random char
        String randomStr = "";
        if (getUrlTag() == 0) {
            randomStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        } else {
            //test
            randomStr = "aaaaaa";
        }
        int firstLen = 16;
        int realLen = Str64.length();
        if (realLen < 16) {
            firstLen = realLen;
        }
        StringBuilder disturbStr64 = new StringBuilder(Str64);
        for (int i = 0; i < firstLen; i++) {
            int seed = random.nextInt(randomStr.length());
            disturbStr64.insert(i * 2 + 1, randomStr.charAt(seed));
        }
        //replace str
        String replaceStr = disturbStr64.toString();
        if (replaceStr.contains("=")) {
            replaceStr = replaceStr.replace("=", "O0O0O");
        }
        if (replaceStr.contains("+")) {
            replaceStr = replaceStr.replace("+", "o000o");
        }
        if (replaceStr.contains("/")) {
            replaceStr = replaceStr.replace("/", "oo00o");
        }
        return replaceStr;
    }
}
