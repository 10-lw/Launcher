package com.onezero.launcher.launcher.presenter;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public interface ITimePresenter {
    void updateTime();
    List<Fragment> getFragmentList();
}
