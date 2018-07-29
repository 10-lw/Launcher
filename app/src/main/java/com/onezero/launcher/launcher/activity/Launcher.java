package com.onezero.launcher.launcher.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.LauncherPageAdapter;
import com.onezero.launcher.launcher.utils.FragmentHelper;

public class Launcher extends AppCompatActivity {


    private ViewPager viewPager;
    private LauncherPageAdapter pageAdapter;

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
    }

    private void initPageView() {
        pageAdapter = new LauncherPageAdapter(getSupportFragmentManager(), FragmentHelper.getFragmentList(this));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
    }

    private void initViews() {
        viewPager = findViewById(R.id.launcher_content_view_pager);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
