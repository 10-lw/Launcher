package com.onezero.launcher.launcher.appInfo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.onezero.launcher.launcher.event.PackageChangedEvent;

import org.greenrobot.eventbus.EventBus;

public class AppChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.d("AppChangeReceiver", "====on AppChangeReceiver receive action: "+action + " packageName:"+packageName);
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            EventBus.getDefault().post(new PackageChangedEvent(packageName, true));
        } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
            EventBus.getDefault().post(new PackageChangedEvent(packageName, false));
        }
    }

}
