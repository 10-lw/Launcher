package com.onezero.launcher.launcher.callback;

import com.onezero.launcher.launcher.model.AppInfo;

import java.util.List;

/**
 * Created by lizeiwei on 2018/7/31.
 */

public interface QueryCallBack {
    void querySuccessful(List<AppInfo> list);
}
