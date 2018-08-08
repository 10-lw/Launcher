package com.onezero.launcher.launcher.appInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.onezero.launcher.launcher.callback.QueryCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class AppInfoUtils {

    private static List<AppInfo> appInfos;

    @SuppressLint("CheckResult")
    public static void queryAllAppInfoTask(final PackageManager pm, final List<String> excludeList, final QueryCallBack callBack) {
        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppInfo>> emitter) throws Exception {
                List<AppInfo> list = queryAllAppInfo(pm, excludeList);
                if (list != null) {
                    emitter.onNext(list);
                } else {
                    emitter.onNext(new ArrayList<AppInfo>());
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<AppInfo>>() {
                    @Override
                    public void accept(List<AppInfo> list) throws Exception {
                        callBack.querySuccessful(list);
                    }
                });
    }

    public static List<AppInfo> queryAllAppInfo(PackageManager pm, List<String> excludeList) {
        if (appInfos == null) {
            appInfos = new ArrayList<>();
        }
        appInfos.clear();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo info : resolveInfoList) {
            AppInfo appInfo = new AppInfo();
            String packageName = info.activityInfo.packageName;
            if (excludeList.contains(packageName)) {
                continue;
            }
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                appInfo.setAppIconId(pm.getApplicationIcon(applicationInfo));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            String activityName = info.activityInfo.name;
            Log.d("tag", "======packageName=========="+packageName);
            appInfo.setIntent(pm.getLaunchIntentForPackage(packageName));

            appInfo.setAppLabel((String) info.loadLabel(pm));
            appInfo.setPkgName(packageName);
            try {
                boolean isSystemApp = ApplicationHelper.isSystemApp(pm.getPackageInfo(packageName, 0));
                appInfo.setSystemApp(isSystemApp);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            appInfos.add(appInfo);
        }

        if (appInfos != null && appInfos.size() > 0) {
            return appInfos;
        }
        Log.w("tag", "query app info error");
        return new ArrayList<>();
    }
}
