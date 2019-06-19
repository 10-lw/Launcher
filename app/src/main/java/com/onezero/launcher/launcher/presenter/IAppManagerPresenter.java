package com.onezero.launcher.launcher.presenter;

import android.content.Context;
import android.content.pm.PackageManager;

import com.onezero.launcher.launcher.appInfo.AppInfo;

import java.util.List;

public interface IAppManagerPresenter {
    void setAppContentView(Context context, PackageManager pm, List<String> excludeList, int hideCounts);
    void setBottomContentView(PackageManager pm, List<String> pkgList);
}
