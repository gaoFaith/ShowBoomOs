package com.showboom.showboomlauncher.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.showboom.showboomlauncher.R;

/**
 * Created by gaopeng on 2018/5/11.
 */

public class WebViewLoadingDialog {

    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.web_loading_dialog, null);
        LinearLayout layout = v.findViewById(R.id.web_dialog_view);
        ProgressBar progress = v.findViewById(R.id.img);
        TextView tipTextView = v.findViewById(R.id.dialog_text_hint);
        progress.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pull_pro_anim));
        tipTextView.setText(msg);
        tipTextView.setVisibility(View.GONE);
        Dialog loadingDialog = new Dialog(context, R.style.progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(250, 250));
        return loadingDialog;

    }
}
