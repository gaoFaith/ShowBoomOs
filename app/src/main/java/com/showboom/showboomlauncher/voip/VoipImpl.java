package com.showboom.showboomlauncher.voip;

import android.content.Context;
import android.widget.ImageView;

import com.showboom.showboomlauncher.R;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.voip.CallFinishEntry;
import com.yuntongxun.plugin.voip.IVoipCallBack;

/**
 * Created by WJ on 2016/11/29.
 */

public class VoipImpl implements IVoipCallBack {
    private static final String TAG = "VoipImpl";
    private static VoipImpl instance;

    public static VoipImpl getInstance() {
        if (instance == null) {
            instance = new VoipImpl();
        }
        return instance;
    }

    /**
     * 通话状态信息
     *
     * @param voIPCall 通话状态信息
     */
    @Override
    public void onCallEvents(ECVoIPCallManager.VoIPCall voIPCall) {
        LogUtil.d(TAG, "[onCallEvents] call:" + voIPCall.callState + ",reason:" + voIPCall.reason);

        if(voIPCall.callState == ECVoIPCallManager.ECCallState.ECCALL_ANSWERED) {
            ECDevice.getECVoIPSetupManager().enableLoudSpeaker(true);
        }else if(voIPCall.callState == ECVoIPCallManager.ECCallState.ECCALL_PROCEEDING) {
            LogUtil.d(TAG, "[onCallEvents] phone brand="+android.os.Build.BRAND);
            if("showboom".equals(android.os.Build.BRAND)) {

            }
        }

    }

    @Override
    public void onCallFinish(CallFinishEntry entry) {
        LogUtil.d(TAG, "[onCallFinish] entry:" + entry);
    }

    /**
     * 通话页面信息绑定接口
     *
     * @param userId 传出的id 开发者可根据此id来从自己数据库等中查询对应的名字和头像
     * @param avater 头像的imageview控件
     */
    @Override
    public void onVoipBindView(Context context, String userId, ImageView avater) {
        //代码示例
        LogUtil.e(TAG, "onVoipBindView userId=" + userId + " avater=" + avater);
        avater.setImageResource(R.mipmap.service);
    }
}
