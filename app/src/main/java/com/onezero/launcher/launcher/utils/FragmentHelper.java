package com.onezero.launcher.launcher.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.fragment.AllAppsFragment;
import com.onezero.launcher.launcher.fragment.DateAppsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class FragmentHelper {
    public static List<Fragment> fragments = new ArrayList<>();

    public static List<Fragment> getFragmentList(Activity context) {
        fragments.clear();
        List<AppInfo> appInfos = AppInfoUtils.queryAllAppInfo(context.getPackageManager());
        int size = appInfos.size();
        Log.d("tag", "=========app size====:::::"+size);
        int firstPageAppCount = context.getResources().getInteger(R.integer.launcher_first_page_app_counts);
        int perPageMaxAppCount = context.getResources().getInteger(R.integer.launcher_per_page_app_max_counts);
        if (size > 0) {
            DateAppsFragment dateAppsFragment = new DateAppsFragment();
            int toIndex = Math.min(size - 1, firstPageAppCount);
//            List<AppInfo> list = appInfos.subList(0, toIndex);
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

}
