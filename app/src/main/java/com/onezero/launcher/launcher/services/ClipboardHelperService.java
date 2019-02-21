package com.onezero.launcher.launcher.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.onezero.launcher.launcher.utils.ClipboardUtil;
import com.onezero.launcher.launcher.utils.StringUtils;

public class ClipboardHelperService extends Service {
    public ClipboardHelperService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String clipboardText = intent.getStringExtra("ClipboardText");
        if (!StringUtils.isNullOrEmpty(clipboardText)) {
            ClipboardUtil.putTextIntoClip(getApplicationContext(), clipboardText);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
