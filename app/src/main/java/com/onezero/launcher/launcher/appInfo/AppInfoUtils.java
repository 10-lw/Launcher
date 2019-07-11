package com.onezero.launcher.launcher.appInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.onezero.launcher.launcher.callback.QueryCallBack;
import com.onezero.launcher.launcher.http.AppListHelper;
import com.onezero.launcher.launcher.http.ApplicationConstant;
import com.onezero.launcher.launcher.model.AppInfo;
import com.onezero.launcher.launcher.model.LauncherItemModel;
import com.onezero.launcher.launcher.model.LauncherItemModel_Table;
import com.onezero.launcher.launcher.utils.NetWorkUtils;
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

    private volatile static List<AppInfo> appInfos;
    private volatile static List<AppInfo> bottomAppInfos;
    private volatile static List<AppInfo> virtualList;
    private static long INTERVAL_TIME = 10 * 60 * 1000; //10min
    private static int mHideCounts = 0;

    @SuppressLint("CheckResult")
    public static void queryAllAppInfoTask(final Context context, final PackageManager pm, final List<String> excludeList, final int hideCounts, final QueryCallBack callBack) {
        mHideCounts = hideCounts;
        Observable
                .create(new ObservableOnSubscribe<List<AppInfo>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<AppInfo>> emitter) {
                        List<AppInfo> list = queryAllAppInfo(pm, excludeList, hideCounts);
                        if (list != null) {
                            emitter.onNext(list);
                        } else {
                            emitter.onNext(new ArrayList<AppInfo>());
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
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
     *
     * @param context
     * @param pm
     * @param excludeList
     * @param callBack
     */
    @SuppressLint("CheckResult")
    public static void queryVirtualAppInfoTask(final Context context, final PackageManager pm, final List<String> excludeList, final int hid, final QueryCallBack callBack) {
        if (NetWorkUtils.pingNet() != 0) {
            Log.d("tag", "queryVirtualAppInfoTask:net is not ok ");
            callBack.querySuccessful(new ArrayList<AppInfo>());
            return;
        }
        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppInfo>> emitter) throws Exception {
                SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.SP_NAME, Context.MODE_PRIVATE);
                long before = sharedPreferences.getLong(ApplicationConstant.SP_VIRTUAL_UPDATE_TIME, System.currentTimeMillis());
                //指定时间内不再重新查询网络, 复用原来的数据
                boolean isTime = System.currentTimeMillis() - before < INTERVAL_TIME;//是否需要重新查询
                if (isTime && virtualList != null) {
                    emitter.onNext(virtualList);
                    return;
                } else {
                }
                virtualList = AppListHelper.getInstance().getAppInfoList(context);
                ArrayList<String> pkgs = new ArrayList<>();
                List<AppInfo> list = queryAllAppInfo(pm, excludeList, hid);
                for (AppInfo info : list) {
                    pkgs.add(info.getPkgName());
                }

                Iterator<AppInfo> iterator = virtualList.iterator();
                while (iterator.hasNext()) {
                    AppInfo next = iterator.next();
                    if (pkgs.contains(next.getPkgName())) {
                        iterator.remove();
                    }
                }
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putLong(ApplicationConstant.SP_VIRTUAL_UPDATE_TIME, System.currentTimeMillis()).apply();
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
        //复用原来的数据
        if (bottomAppInfos == null) {
            bottomAppInfos = new ArrayList<>();
        } else {
            return bottomAppInfos;
        }
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

    public static void clearData() {
        if (appInfos != null) {
            appInfos.clear();
            appInfos = null;
        }
    }

    //新添加数据时,直接加入集合,避免复用数据时数据错误
    public static void addDataToLast(PackageManager pm, String packageName) {
        if (appInfos == null) {
            appInfos = new ArrayList<>();
        }
        AppInfo info = AppInfoUtils.getAppInfoByPkgName(pm, packageName);
        Where<LauncherItemModel> where = new Select()
                .from(LauncherItemModel.class)
                .where(LauncherItemModel_Table.apkPkg.eq(info.getPkgName()));
        LauncherItemModel q = where.querySingle();
        int size = appInfos.size();
        int vir = virtualList == null ? 0 : virtualList.size();
        info.setPosition(q == null ? (size + mHideCounts + vir) : q.position);
        appInfos.add(size, info);
        deleteVirtualListItem(packageName);
    }

    //如果是安装的虚拟的应用, 这对应的需要把虚拟图标数据删除
    private static void deleteVirtualListItem(String packageName) {
        deleteDateFromList(virtualList, packageName);
    }

    private static void deleteDateFromList(List<AppInfo> list, String pkg) {
        if (list == null || list.size() == 0) {
            return;
        }
        Iterator<AppInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            AppInfo next = iterator.next();
            if (next.getPkgName().equals(pkg)) {
                iterator.remove();
            }
        }
    }

    //卸载应用时需要将原数据集合的内容清除, 避免产生假的数据, 不再恢复虚拟应用数据, 因为每间隔 #INTERVAL_TIME 会重新查一次
    public static void delAllAppListItem(String pkgName) {
        deleteDateFromList(appInfos, pkgName);
    }

    public synchronized static List<AppInfo> queryAllAppInfo(PackageManager pm, List<String> excludeList, int hideCounts) {
        //复用原来的数据
        if (appInfos == null) {
            appInfos = new ArrayList<>();
        } else {
            return appInfos;
        }
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
                appInfo.setPosition(size + hideCounts);
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
            appDataList.add(new AppInfo(false, false, "nullPkg " + i, i));
        }
    }
}
