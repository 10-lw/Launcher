package com.onezero.launcher.launcher.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.callback.CalculateCallBack;
import com.onezero.launcher.launcher.callback.QueryCallBack;
import com.onezero.launcher.launcher.fragment.AllAppsFragment;
import com.onezero.launcher.launcher.fragment.DateAppsFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class FragmentHelper {

    public static void getFragmentList(final Activity context, List<String> excludeList, final CalculateCallBack calculateCallBack) {
        AppInfoUtils.queryAllAppInfoTask(context.getPackageManager(), excludeList, new QueryCallBack() {
            @Override
            public void querySuccessful(final List<AppInfo> list) {
                Observable.create(new ObservableOnSubscribe<List<Fragment>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Fragment>> emitter) throws Exception {
                        emitter.onNext(calculateFragments(context, list));
                    }
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Fragment>>() {
                            @Override
                            public void accept(List<Fragment> fragments) throws Exception {
                                calculateCallBack.calculateSuccessful(fragments);
                            }
                        });
            }
        });
    }

    private static List<Fragment> calculateFragments(Activity context, List<AppInfo> appInfos) {
        List<Fragment> fragments = new ArrayList<>();
        int size = appInfos.size();
        Log.d("tag", "=========sie="+size);
        int firstPageAppCount = context.getResources().getInteger(R.integer.launcher_first_page_app_counts);
        int perPageMaxAppCount = context.getResources().getInteger(R.integer.launcher_per_page_app_max_counts);
        if (size > 0) {
            DateAppsFragment dateAppsFragment = new DateAppsFragment();
            int toIndex = Math.min(size - 1, firstPageAppCount);
            List<AppInfo> firstPage = new ArrayList<>();
            for (int i = 0; i < toIndex - 0; i++) {
                firstPage.add(appInfos.get(i));
            }
            dateAppsFragment.setAppInfos(firstPage);
            fragments.add(dateAppsFragment);
        }

        if (size > firstPageAppCount) {
            int remain = size - firstPageAppCount;
            int pages = remain / perPageMaxAppCount + 1;
            for (int i = 0; i < pages; i++) {
                AllAppsFragment allAppsFragment = new AllAppsFragment();
                int fromIndex = firstPageAppCount + perPageMaxAppCount * i;
                int toIndex = Math.min(((firstPageAppCount + (i + 1) * perPageMaxAppCount)) - 1, size - 1);
                List<AppInfo> perPageList = new ArrayList<>();
                for (int j = fromIndex; j < toIndex; j++) {
                    perPageList.add(appInfos.get(j));
                }
                allAppsFragment.setAppInfos(perPageList);
                fragments.add(allAppsFragment);
            }
        }
        return fragments;
    }


    public static List<AppInfo> resetRemoveIcon(List<AppInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRemoveable(false);
        }
        return list;
    }
}
