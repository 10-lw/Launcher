package com.onezero.launcher.launcher.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.LauncherPageAdapter;
import com.onezero.launcher.launcher.appInfo.ApplicationHelper;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemLongClickEvent;
import com.onezero.launcher.launcher.utils.FragmentHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Launcher extends AppCompatActivity {


    private ViewPager viewPager;
    private LauncherPageAdapter pageAdapter;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPageView();
        EventBus.getDefault().register(this);
        if (viewPager != null) {
            viewPager.setCurrentItem(currentPosition);
        }
    }

    private void initPageView() {
        pageAdapter = new LauncherPageAdapter(getSupportFragmentManager(), FragmentHelper.getFragmentList(this));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        viewPager = findViewById(R.id.launcher_content_view_pager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onItemClick(OnAppItemClickEvent event) {
        Log.d("tag", "===click package name====="+event.getInfo().getPkgName());
        ApplicationHelper.performStartApp(this, event.getInfo());
    }

    @Subscribe
    public void onItemClick(OnAppItemLongClickEvent event) {
        Log.d("tag", "===long click package name====="+event.getInfo().getPkgName());
    }
}
