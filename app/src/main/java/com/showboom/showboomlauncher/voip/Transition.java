package com.showboom.showboomlauncher.voip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.plugin.common.common.BackwardSupportUtil;
import com.yuntongxun.plugin.common.common.dialog.CCPAlertBuilder;
import com.yuntongxun.plugin.common.common.dialog.CCPAlertDialog;
import com.yuntongxun.plugin.common.common.utils.TextUtil;
import com.yuntongxun.plugin.voip.CallService;
import com.yuntongxun.plugin.voip.Voip;

public class Transition {

    public static void startCallAction(Context ctx, ECVoIPCallManager.CallType callType, String nickname, String contactId, String phoneNumber, boolean isCallBack) {
        if (!isConnected(ctx)) {
            showNetworkUnavailable(ctx);
        } else if(checkCallOperation(Voip.getCallService(), contactId, phoneNumber, callType)){
            Intent action = buildCallIntent(callType, nickname, contactId, phoneNumber, (String)null, isCallBack);
            Voip.getCallService().startCall(action);
        }
    }

    public static boolean isConnected(Context ctx) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                isConnected = networkInfo.isConnected();
            }
        }
        return isConnected;
    }

    public static boolean checkCallOperation(CallService callService, String called, String phoneNumber, ECVoIPCallManager.CallType type) {
        if (callService.getCallEntry() == null) {
            return true;
        } else {
            if (callService.isHoldingCall()) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static Intent buildCallIntent(ECVoIPCallManager.CallType callType, String nickname, String contactId, String phoneNumber, String voipFrom, boolean isCallBack) {
        Intent action = new Intent();
        action.putExtra("com.ccp.phone.call_type", callType);
        action.putExtra("com.yuntongxun.rongxin.CALL_NUMBER", contactId);
        if (voipFrom != null) {
            action.putExtra("com.yuntongxun.rongxin.CALL_SOURCE", voipFrom);
        }

        action.putExtra("com.yuntongxun.rongxin.CALL_USERNAME", !TextUtil.isEmpty(nickname) ? nickname : phoneNumber);
        if (!BackwardSupportUtil.isNullOrNil(phoneNumber)) {
            action.putExtra("com.yuntongxun.rongxin.CALL_PHONE_NUMBER", phoneNumber);
        }

        action.putExtra("com.yuntongxun.rongxin.CALL_NUMBER", contactId);
        action.putExtra("com.yuntongxun.rongxin.CALL_OUT", true);
        action.putExtra("com.yuntongxun.rongxin.CALL_BACK", isCallBack);
        return action;
    }

    public static void showNetworkUnavailable(Context ctx) {
        CCPAlertBuilder alertBuilder = new CCPAlertBuilder(ctx);
        alertBuilder.setTitle(com.yuntongxun.plugin.voip.R.string.app_tip);
        alertBuilder.setMessage(com.yuntongxun.plugin.voip.R.string.voip_load_failed_network).setPositiveButton(com.yuntongxun.plugin.voip.R.string.app_ok, (DialogInterface.OnClickListener)null);
        CCPAlertDialog create = alertBuilder.create();
        create.show();
    }
}
