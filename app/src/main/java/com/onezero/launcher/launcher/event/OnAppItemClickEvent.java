package com.onezero.launcher.launcher.event;

import com.onezero.launcher.launcher.appInfo.AppInfo;

public class OnAppItemClickEvent {
    private AppInfo info;

    public OnAppItemClickEvent(AppInfo info) {
        this.info = info;
    }

    public AppInfo getInfo() {
        return info;
    }

    public void setInfo(AppInfo info) {
        this.info = info;
    }
}
