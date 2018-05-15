package com.showboom.showboomlauncher.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by gaopeng on 2018/3/20.
 */

public class T {
    public static Context mContext;
    private static Toast toast;


    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static void register(Context context) {
        mContext = context;
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message) {
        if (mContext == null) {
            throw new RuntimeException("unRegister Context in Application");
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }

    public static void showShort(int resId) {
        if (mContext == null) {
            throw new RuntimeException("unRegister Context in Application");
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT);
        toast.setText(mContext.getString(resId));
        toast.show();
    }

}
