package com.showboom.showboomlauncher.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.showboom.showboomlauncher.R;

/**
 * 加载动画
 */
public class CustomConfirmDialog {
    private static BtnClickListener mlistener;

    public static Dialog createDialog(Context context, BtnClickListener listener, String msg) {
        Log.d("CustomConfirmDialog", "createLoadingDialog");
        final Dialog cusDialog = new Dialog(context, R.style.progress_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        mlistener = listener;
        View v = null;
        LinearLayout layout = null;
        v = inflater.inflate(R.layout.confirm_dialog_layout, null);
        layout = (LinearLayout) v.findViewById(R.id.lay_force_layout);
        TextView hintTitle = (TextView) v.findViewById(R.id.hint);
        TextView hintContent = (TextView) v.findViewById(R.id.hint_content);
        Button cancelBtn = (Button) v.findViewById(R.id.cancel);
        Button sureBtn = (Button) v.findViewById(R.id.sure);
        hintTitle.setText("提示");
        cancelBtn.setText("取消");
        sureBtn.setText("确定");
        hintContent.setText(msg);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.cancel();
                if (cusDialog.isShowing()) {
                    cusDialog.dismiss();
                }
            }
        });
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.sure();
                if (cusDialog.isShowing()) {
                    cusDialog.dismiss();
                }
            }
        });

        Log.d("CustomConfirmDialog", "cusDialog=" + cusDialog.toString());
        //cusDialog.setCancelable(false);
        cusDialog.setContentView(layout, new LinearLayout.LayoutParams(540, 300));

        return cusDialog;

    }

    public interface BtnClickListener {
        void sure();
        void cancel();
    }
}