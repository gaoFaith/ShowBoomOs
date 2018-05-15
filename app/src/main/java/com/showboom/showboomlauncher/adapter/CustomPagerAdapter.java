package com.showboom.showboomlauncher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.showboom.showboomlauncher.bean.ScreenInfo;
import com.showboom.showboomlauncher.fragment.CustomFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaopeng on 2018/5/7.
 */

public class CustomPagerAdapter extends BaseFragmentPagerAdapter {

    private List<String> lrcs = new ArrayList<>();
    private ScreenInfo info;

    public CustomPagerAdapter(FragmentManager fm, ScreenInfo info) {
        super(fm);
        this.info = info;
    }

    public void addDatas(List<String> lrcs) {
        this.lrcs.addAll(lrcs);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return CustomFragment.newInstance(info, lrcs.get(position), position);
    }

    //除了给定位置，其他位置的Fragment进行刷新
    public void notifyChangeWithoutPosition(int position) {
        String valueP = tags.valueAt(position);
        tags.clear();
        tags.put(position, valueP);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % lrcs.size();
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return lrcs.size();
//        return Integer.MAX_VALUE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
