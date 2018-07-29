package com.onezero.launcher.launcher.callback;

import com.onezero.launcher.launcher.appInfo.AppInfo;

public interface RecyclerViewClickListener {
    void onItemClick(AppInfo info);
    void onRemoveClick(AppInfo info);
}
