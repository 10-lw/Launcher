package com.onezero.launcher.launcher;

import android.app.Application;

import com.onezero.launcher.launcher.http.HttpUtils;
import com.onezero.launcher.launcher.http.ImageDownLoader;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.picasso.Picasso;

public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        initPicasso();
    }

    /**
     * 初始化picasso使用okhttp作为网络请求框架
     */
    private void initPicasso() {
        Picasso.setSingletonInstance(new Picasso.Builder(this).
                downloader(new ImageDownLoader(HttpUtils.getSOkHttpClient())).loggingEnabled(true)
                .build());

    }
}
