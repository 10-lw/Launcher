package com.onezero.launcher.launcher.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import com.onezero.launcher.launcher.model.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.callback.QueryCallBack;
import com.onezero.launcher.launcher.model.TimeModel;
import com.onezero.launcher.launcher.view.IAppContentView;
import com.onezero.launcher.launcher.view.ITimeView;

import java.util.List;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class LauncherPresenter implements ITimePresenter, IAppManagerPresenter {
    private final TimeModel model;
    private ITimeView view;
    private IAppContentView appView;


    public LauncherPresenter(ITimeView view, IAppContentView appView) {
        model = new TimeModel();
        this.view = view;
        this.appView = appView;
    }

    public LauncherPresenter(ITimeView view) {
        model = new TimeModel();
        this.view = view;
    }

    @Override
    public void updateTime() {
        String week = model.getWeekText();
        String date = model.getDateText();
        view.updateDate(date, week);
    }

    @Override
    public List<Fragment> getFragmentList() {
        return null;
    }

    @Override
    public void setAppContentView(Context context, PackageManager pm, List<String> excludeList, int hideCounts) {
        AppInfoUtils.queryAllAppInfoTask(context, pm, excludeList, hideCounts, new QueryCallBack() {
            @Override
            public void querySuccessful(List<AppInfo> list) {
                appView.layoutAllAppsContent(list);
            }
        });
    }

    @Override
    public void setBottomContentView(PackageManager pm, List<String> bottomAppsConfigs) {
        AppInfoUtils.queryBottomAppInfoTask(pm, bottomAppsConfigs, new QueryCallBack() {
            @Override
            public void querySuccessful(List<AppInfo> list) {
                appView.layoutBottomAppContent(list);
            }
        });
    }
}
