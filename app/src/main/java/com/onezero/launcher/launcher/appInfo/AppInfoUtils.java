package com.onezero.launcher.launcher.appInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import com.onezero.launcher.launcher.callback.LoadFinishCallback;
import com.onezero.launcher.launcher.callback.QueryCallBack;
import com.onezero.launcher.launcher.http.AppListHelper;
import com.onezero.launcher.launcher.model.LauncherItemModel;
import com.onezero.launcher.launcher.model.LauncherItemModel_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/7/28.
 */

public class AppInfoUtils {

    private static List<AppInfo> appInfos;

    @SuppressLint("CheckResult")
    public static void queryAllAppInfoTask(final Context context, final PackageManager pm, final List<String> excludeList, final int hideCounts, final QueryCallBack callBack) {
        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppInfo>> emitter) throws Exception {
                ArrayList<String> pkgs = new ArrayList<>();
                List<AppInfo> list = queryAllAppInfo(pm, excludeList, hideCounts);
                for (int i = 0; i < list.size(); i++) {
                    pkgs.add(list.get(i).getPkgName());
                }

                List<AppInfo> virtualList = AppListHelper.getInstance().getAppInfoList(context);
                Iterator<AppInfo> iterator = virtualList.iterator();
                while (iterator.hasNext()) {
                    AppInfo next = iterator.next();
                    if (pkgs.contains(next.getPkgName())) {
                        iterator.remove();
                    }
                }

                list.addAll(list.size(), virtualList);
                if (list != null) {
                    emitter.onNext(list);
                } else {
                    emitter.onNext(new ArrayList<AppInfo>());
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AppInfo>>() {
                    @Override
                    public void accept(List<AppInfo> list) throws Exception {
                        callBack.querySuccessful(list);
                    }
                });
    }


    /**
     * 查询虚拟图标应用，排除已安装的应用
     * @param context
     * @param pm
     * @param excludeList
     * @param callBack
     */
    @SuppressLint("CheckResult")
    public static void queryVirsualAppInfoTask(final Context context, final PackageManager pm, final List<String> excludeList, final int hid, final QueryCallBack callBack) {
        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppInfo>> emitter) throws Exception {
                ArrayList<String> pkgs = new ArrayList<>();
                List<AppInfo> list = queryAllAppInfo(pm, excludeList, hid);
                for (AppInfo info : list ) {
                    pkgs.add(info.getPkgName());
                }

                List<AppInfo> virtualList = AppListHelper.getInstance().getAppInfoList(context);
                Iterator<AppInfo> iterator = virtualList.iterator();
                while (iterator.hasNext()) {
                    AppInfo next = iterator.next();
                    if (pkgs.contains(next.getPkgName())) {
                        iterator.remove();
                    }
                }

                if (virtualList != null) {
                    emitter.onNext(virtualList);
                } else {
                    emitter.onNext(new ArrayList<AppInfo>());
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AppInfo>>() {
                    @Override
                    public void accept(List<AppInfo> list) throws Exception {
                        callBack.querySuccessful(list);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public static void queryBottomAppInfoTask(final PackageManager pm, final List<String> bottomAppsConfigs, final QueryCallBack callBack) {
        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppInfo>> emitter) throws Exception {
                List<AppInfo> list = queryBottomAppInfo(pm, bottomAppsConfigs);

                if (list != null) {
                    emitter.onNext(list);
                } else {
                    emitter.onNext(new ArrayList<AppInfo>());
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AppInfo>>() {
                    @Override
                    public void accept(List<AppInfo> list) throws Exception {
                        callBack.querySuccessful(list);
                    }
                });
    }

    public synchronized static List<AppInfo> queryBottomAppInfo(PackageManager pm, List<String> bottomAppsConfigs) {
        List<AppInfo> bottomAppInfos = new ArrayList<>();
        ArrayList<String> installedPkg = new ArrayList<>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(mainIntent, 0);
        for (int i = 0; i < resolveInfoList.size(); i++) {
            ResolveInfo info = resolveInfoList.get(i);
            String packageName = info.activityInfo.packageName;
            installedPkg.add(packageName);
        }

        for (int i = 0; i < bottomAppsConfigs.size(); i++) {
            String s = bottomAppsConfigs.get(i);
            if (installedPkg.contains(s)) {
                AppInfo appInfo = AppInfoUtils.getAppInfoByPkgName(pm, s);
                if (appInfo.getIntent() != null) {
                    bottomAppInfos.add(appInfo);
                }
            }
        }
        return bottomAppInfos;
    }

    public synchronized static List<AppInfo> queryAllAppInfo(PackageManager pm, List<String> excludeList, int hideCounts) {
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
            appInfo.setIntent(pm.getLaunchIntentForPackage(packageName));

            appInfo.setAppLabel((String) info.loadLabel(pm));
            appInfo.setPkgName(packageName);
            appInfo.setVisiable(true);
            appInfo.setRemoveable(false);

            Where<LauncherItemModel> where = new Select().from(LauncherItemModel.class).where(LauncherItemModel_Table.apkPkg.eq(packageName));
            LauncherItemModel model = where.querySingle();
            if (model != null) {
                appInfo.setPosition(model.position);
            } else {
                int size = appInfos.size();
                appInfo.setPosition(size+ hideCounts);
            }

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

    public static AppInfo getAppInfoByPkgName(PackageManager pm, String pkgName) {
        AppInfo appInfo = new AppInfo();

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            Intent launchIntentForPackage = pm.getLaunchIntentForPackage(pkgName);
            CharSequence label = pm.getApplicationLabel(applicationInfo);
            appInfo.setPkgName(pkgName);
            appInfo.setIntent(launchIntentForPackage);
            appInfo.setAppLabel(label.toString());
            appInfo.setAppIconId(pm.getApplicationIcon(applicationInfo));
            appInfo.setRemoveable(false);
            appInfo.setVisiable(true);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    public static void getDefaultDataList(List<AppInfo> appDataList, int hideCounts) {
        for (int i = 0; i < hideCounts; i++) {
            appDataList.add(new AppInfo(false, false,"nullPkg "+i, i));
        }
    }
}
