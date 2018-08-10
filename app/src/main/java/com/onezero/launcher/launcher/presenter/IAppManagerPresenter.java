package com.onezero.launcher.launcher.presenter;

import android.content.pm.PackageManager;

import com.onezero.launcher.launcher.appInfo.AppInfo;

import java.util.List;

public interface IAppManagerPresenter {
    void setAppContentView(PackageManager pm, List<String> excludeList);
    void setBottomContentView(PackageManager pm, List<String> pkgList);
}
