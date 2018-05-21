package com.showboom.showboomlauncher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.activity.HomeActivity;
import com.showboom.showboomlauncher.bean.ScreenInfo;
import com.showboom.showboomlauncher.widget.HomeView;
import com.showboom.showboomlauncher.widget.HomeWebView;

/**
 * Created by gaopeng on 2018/5/7.
 */

public class CustomFragment extends Fragment {
    public static final String BUNDLE_KEY = "screen_key";
    public static final String BUNDLE_TAG_KEY = "screen_tag_key";
    public static final String BUNDLE_POSITION_KEY = "screen_position_key";
    private ScreenInfo info;
    private String tag;
    private int layoutResId;
    private int position;
    private HomeActivity activity;
    public static Fragment newInstance(ScreenInfo info, String s, int position) {
        CustomFragment fragment = new CustomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY, info);
        bundle.putString(BUNDLE_TAG_KEY, s);
        bundle.putInt(BUNDLE_POSITION_KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (HomeActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            info = (ScreenInfo) bundle.getSerializable(BUNDLE_KEY);
        }
        if (info != null) {
            layoutResId = info.getLayoutId();
        }
        tag = bundle.getString(BUNDLE_TAG_KEY);
        position = bundle.getInt(BUNDLE_POSITION_KEY, -1);
        //Log.d(App.TAG, "layoutResId" + layoutResId + ";screen_tag_key=" + tag + ";position=" + position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        switch (position) {
            case 0:

                break;
            case 1:
                view = inflater.inflate(R.layout.home_content_layout, container, false);
                ((HomeView) view).setContext(this.getContext(),activity);
                break;
            case 2:
                view = inflater.inflate(R.layout.home_web_view_layout, container, false);
                ((HomeWebView) view).setContext(this.getContext(),"http://www.showboom.cn/round/index.html#/");
                break;
            case 3:
                view = inflater.inflate(R.layout.activity_app, container, false);
                //((AppStaticView) view).setContext(this.getContext(),activity);
                break;
        }
        return view;
    }

}
