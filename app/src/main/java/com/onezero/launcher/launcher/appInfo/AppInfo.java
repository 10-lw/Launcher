package com.onezero.launcher.launcher.appInfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class AppInfo {
    private String appLabel;    //应用程序标签
    private Drawable appIconId ;  //应用程序图像
    private Intent intent ;     //启动应用程序的Intent ，一般是Action为Main和Category为Lancher的Activity
    private String pkgName ;    //应用程序所对应的包名
    private boolean isSystemApp;
    private boolean removeable;

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }


    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public Drawable getAppIconId() {
        return appIconId;
    }

    public void setAppIconId(Drawable appIconId) {
        this.appIconId = appIconId;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public AppInfo() {
    }

    public boolean isRemoveable() {
        return removeable;
    }

    public void setRemoveable(boolean removeable) {
        this.removeable = removeable;
    }

    public AppInfo(String appLabel, Drawable appIconId, Intent intent, String pkgName) {
        this.appLabel = appLabel;
        this.appIconId = appIconId;
        this.intent = intent;
        this.pkgName = pkgName;
    }
}
