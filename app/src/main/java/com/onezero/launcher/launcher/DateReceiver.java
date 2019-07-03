package com.onezero.launcher.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.onezero.launcher.launcher.http.ApplicationConstant;
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
        } else if(ApplicationConstant.DOWNLOAD_ACTION.equals(action)) {
            boolean status = intent.getBooleanExtra("status", false);
            String pkgName = intent.getStringExtra("pkgName");
            if (!status) {
                presenter.notifyItemLabel(pkgName);
            }
        } else if(ApplicationConstant.INSTALL_APP_ERROR_ACTION.equals(action)) {
            String pkgName = intent.getStringExtra("pkgName");
            presenter.notifyItemLabel(pkgName);
        }
    }
}
