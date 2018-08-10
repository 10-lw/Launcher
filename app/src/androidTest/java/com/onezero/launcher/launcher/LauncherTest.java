package com.onezero.launcher.launcher;

import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.onezero.launcher.launcher.activity.UnitTestActivity;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.utils.DeviceConfig;

import org.junit.Test;

import java.util.List;

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

}
