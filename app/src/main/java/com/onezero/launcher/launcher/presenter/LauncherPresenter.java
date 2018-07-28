package com.onezero.launcher.launcher.presenter;

import android.support.v4.app.Fragment;

import com.onezero.launcher.launcher.model.TimeModel;
import com.onezero.launcher.launcher.view.ITimeView;

import java.util.List;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class LauncherPresenter implements ITimePresenter {
    private final TimeModel model;
    private ITimeView view;

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
}
