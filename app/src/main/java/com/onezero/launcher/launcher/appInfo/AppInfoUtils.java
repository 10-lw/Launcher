package com.onezero.launcher.launcher.appInfo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class AppInfoUtils {

    private static ArrayList<AppInfo> appInfos;

    public static ArrayList<AppInfo> queryAllAppInfo(PackageManager pm) {
        if (appInfos == null) {
            appInfos = new ArrayList<>();
        }
        appInfos.clear();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(pm));
        Intent intent = new Intent();
        for (ResolveInfo info : resolveInfoList) {
            AppInfo appInfo = new AppInfo();
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(info.activityInfo.packageName, PackageManager.GET_META_DATA);
                appInfo.setAppIconId(pm.getApplicationIcon(applicationInfo));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            String activityName = info.activityInfo.name;
            String packageName = info.activityInfo.packageName;
            appInfo.setIntent(pm.getLaunchIntentForPackage(packageName));

            appInfo.setAppLabel((String) info.loadLabel(pm));
            appInfo.setPkgName(packageName);

            appInfos.add(appInfo);
        }

        if (appInfos != null && appInfos.size() > 0) {
            return appInfos;
        }
        Log.w("tag", "query app info error");
        return new ArrayList<>();
    }
}
