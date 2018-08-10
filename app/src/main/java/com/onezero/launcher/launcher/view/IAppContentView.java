package com.onezero.launcher.launcher.view;

import com.onezero.launcher.launcher.appInfo.AppInfo;

import java.util.List;

public interface IAppContentView {
    void layoutAllAppsContent(List<AppInfo> list);
    void layoutBottomAppContent(List<AppInfo> list);
}
