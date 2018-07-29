package com.onezero.launcher.launcher.callback;

import com.onezero.launcher.launcher.appInfo.AppInfo;

public interface RecyclerViewClickListener {
    void OnItemClick(AppInfo info);
    void OnLongClick(AppInfo info);
}
