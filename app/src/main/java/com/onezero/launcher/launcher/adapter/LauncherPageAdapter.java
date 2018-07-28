package com.onezero.launcher.launcher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class LauncherPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public LauncherPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<Fragment> list){
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
