package com.onezero.launcher.launcher.appInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
    public static boolean isSystemApp(PackageInfo pInfo) {
        //判断是否是系统软件
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        //判断是否是软件更新..
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }
    public static boolean isUserApp(PackageInfo pInfo) {
        //是否是系统软件或者是系统软件正在更新
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }
}
