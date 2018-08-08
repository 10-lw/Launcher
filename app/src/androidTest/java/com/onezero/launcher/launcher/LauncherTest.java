package com.onezero.launcher.launcher;

import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.onezero.launcher.launcher.activity.UnitTestActivity;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.callback.CalculateCallBack;
import com.onezero.launcher.launcher.fragment.BaseAppFragment;
import com.onezero.launcher.launcher.utils.DeviceConfig;
import com.onezero.launcher.launcher.utils.FragmentHelper;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LauncherTest extends ActivityInstrumentationTestCase2<UnitTestActivity> {

    private UnitTestActivity mActivity;

    public LauncherTest() {
        super(UnitTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
    }

    @Test
    public void testQueryAllApps() throws Exception {
        PackageManager pm = mActivity.getPackageManager();
        List<String> excludeAppsConfigs = DeviceConfig.getInstance(mActivity).getExcludeAppsConfigs();
        List<AppInfo> appInfos = AppInfoUtils.queryAllAppInfo(pm, excludeAppsConfigs);
        Log.d("tag", "-=============" + appInfos.size());
        assertTrue(appInfos != null);
        assertTrue(appInfos.size() > 0);

    }

    @Test
    public void testGetFragmentList() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> excludeAppsConfigs = DeviceConfig.getInstance(mActivity).getExcludeAppsConfigs();
        if (mActivity != null) {
            FragmentHelper.getFragmentList(mActivity, excludeAppsConfigs, new CalculateCallBack() {
                @Override
                public void calculateSuccessful(List<Fragment> list) {
                    List<Fragment> fragmentList = list;
                    assertTrue(fragmentList != null);
                    assertTrue(fragmentList.size() >= 2);

                    BaseAppFragment fragment = (BaseAppFragment) fragmentList.get(0);
                    int firstPageCount = mActivity.getResources().getInteger(R.integer.launcher_first_page_app_counts);
                    assertTrue(fragment.getAppListInfo().size() == firstPageCount);


                    BaseAppFragment dateAppFragment = (BaseAppFragment) fragmentList.get(0);
                    int perPageMaxCount = mActivity.getResources().getInteger(R.integer.launcher_per_page_app_max_counts);
                    assertTrue(dateAppFragment.getAppListInfo().size() <= perPageMaxCount);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.wait();
    }

}
