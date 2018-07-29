package com.onezero.launcher.launcher.appInfo;

import android.content.Context;
import android.util.Log;

public class ApplicationHelper {
    public static final String TAG = "ApplicationHelper";
    public static void performStartApp(Context context, AppInfo info){
        startActivitySafely(context, info);
    }

    private static void startActivitySafely(Context context, AppInfo info) {
        try {
            context.startActivity(info.getIntent());
        } catch (Exception e) {
            Log.e(TAG, "start activity error,the intent is:"+info.getIntent().toString());
            e.printStackTrace();
        }
    }
}
