package com.onezero.launcher.launcher.fragment;

import com.onezero.launcher.launcher.appInfo.AppInfo;

import java.util.List;

public abstract class BaseAppFragment extends BaseFragment {
    public abstract void setAppInfos(List<AppInfo> list);
    public abstract List<AppInfo> getAppListInfo();
}
