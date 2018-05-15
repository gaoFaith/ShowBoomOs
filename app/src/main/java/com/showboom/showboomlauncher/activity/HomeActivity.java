package com.showboom.showboomlauncher.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.adapter.CustomPagerAdapter;
import com.showboom.showboomlauncher.bean.ScreenInfo;
import com.showboom.showboomlauncher.widget.HomeView;

import java.util.Arrays;

public class HomeActivity extends BaseActivity implements HomeView.HomeRecommendListener {
    private ViewPager fragmentViewPager;
    private CustomPagerAdapter pagerAdapter;
    private FragmentManager fragmentManager;
    private String[] tags = {"0", "1", "2", "4"};

    @Override
    public int initContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void initUIAndListener() {
        fragmentViewPager = findViewById(R.id.home_view_pager);

        fragmentManager = getSupportFragmentManager();
        ScreenInfo info = new ScreenInfo();
        info.setLayoutId(R.layout.home_content_layout);
        info.setTag("1");

        pagerAdapter = new CustomPagerAdapter(fragmentManager, info);

        pagerAdapter.addDatas(Arrays.asList(tags));
        fragmentViewPager.setAdapter(pagerAdapter);
        fragmentViewPager.setCurrentItem(1);
        //fragmentViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showRecommendLayout() {
        fragmentViewPager.setCurrentItem(2,true);
    }
}
