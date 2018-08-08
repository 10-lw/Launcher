package com.onezero.launcher.launcher.activity;

import android.app.WallpaperManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.LauncherPageAdapter;
import com.onezero.launcher.launcher.appInfo.ApplicationHelper;
import com.onezero.launcher.launcher.callback.CalculateCallBack;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemRemoveClickEvent;
import com.onezero.launcher.launcher.utils.DeviceConfig;
import com.onezero.launcher.launcher.utils.FragmentHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class Launcher extends AppCompatActivity {


    private ViewPager viewPager;
    private LauncherPageAdapter pageAdapter;
    private int currentPosition;
    private RecyclerView bottomContent;
    private List<String> bottomAppsConfigs;
    private List<String> excludeAppsConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Drawable wallPaper = WallpaperManager.getInstance(this).getDrawable();
        this.getWindow().setBackgroundDrawable(wallPaper);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initPageView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(currentPosition);
        }
    }

    private void initData() {
        bottomAppsConfigs = DeviceConfig.getInstance(this).getBottomAppsConfigs();
        excludeAppsConfigs = DeviceConfig.getInstance(this).getExcludeAppsConfigs();
    }

    private void initPageView() {
        FragmentHelper.getFragmentList(this, excludeAppsConfigs, new CalculateCallBack() {
            @Override
            public void calculateSuccessful(List<Fragment> list) {
                pageAdapter = new LauncherPageAdapter(getSupportFragmentManager(), list);
                viewPager.setOffscreenPageLimit(2);
                viewPager.setAdapter(pageAdapter);
            }
        });
    }

    private void initViews() {
        viewPager = findViewById(R.id.launcher_content_view_pager);
        bottomContent = findViewById(R.id.launcher_bottom_area);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onItemClick(OnAppItemClickEvent event) {
        ApplicationHelper.performStartApp(this, event.getInfo());
    }

    @Subscribe
    public void onItemRemoveClick(OnAppItemRemoveClickEvent event) {
        ApplicationHelper.performUninstallApp(this, event.getInfo());
    }
}
