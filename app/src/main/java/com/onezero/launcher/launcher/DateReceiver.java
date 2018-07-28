package com.onezero.launcher.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.onezero.launcher.launcher.presenter.LauncherPresenter;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class DateReceiver extends BroadcastReceiver {

    private final LauncherPresenter presenter;

    public DateReceiver(LauncherPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String  action = intent.getAction();
        if (action.equals(Intent.ACTION_DATE_CHANGED) || action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
            presenter.updateTime();
        }
    }
}
