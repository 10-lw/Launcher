package com.onezero.launcher.launcher.view;

import com.onezero.launcher.launcher.model.AppInfo;

import java.util.List;

public interface IAppContentView {
    void layoutAllAppsContent(List<AppInfo> list);
    void layoutBottomAppContent(List<AppInfo> list);
    void notifyItemLabel(String pkg);
}
