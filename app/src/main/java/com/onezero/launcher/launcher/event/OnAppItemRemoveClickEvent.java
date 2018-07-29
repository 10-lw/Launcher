package com.onezero.launcher.launcher.event;

import com.onezero.launcher.launcher.appInfo.AppInfo;

public class OnAppItemRemoveClickEvent {
    private AppInfo info;

    public AppInfo getInfo() {
        return info;
    }

    public void setInfo(AppInfo info) {
        this.info = info;
    }

    public OnAppItemRemoveClickEvent(AppInfo info) {
        this.info = info;
    }
}
