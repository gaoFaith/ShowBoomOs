package com.showboom.showboomlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.showboom.showboomlauncher.App;
import com.showboom.showboomlauncher.service.LauncherDataIntentService;

/**
 * Created by gaopeng on 2018/5/17.
 */

public class ShowBoomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(App.TAG, "ShowBoomReceiver=" + action);
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return;
            NetworkInfo info = manager.getActiveNetworkInfo();
            Log.i(App.TAG, "NetworkInfo=");
            if (info != null && info.isConnected()) {
                Log.i(App.TAG, "startService=" );
                LauncherDataIntentService.startService(context);
//                context.startService(new Intent(context, LauncherDataIntentService.class));
            }
        }
    }
}
