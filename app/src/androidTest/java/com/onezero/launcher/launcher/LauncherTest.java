package com.onezero.launcher.launcher;

import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.onezero.launcher.launcher.activity.UnitTestActivity;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.model.LauncherItemModel;
import com.onezero.launcher.launcher.model.LauncherItemModel_Table;
import com.onezero.launcher.launcher.utils.DeviceConfig;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;

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

    @Test
    public void getDefaultDataList() {
        //SQLite.delete(LauncherItemModel.class).where(LauncherItemModel_Table.apkPkg.eq("com.ss.android.article.news")).execute();
        SQLite.delete(LauncherItemModel.class)
                .where(LauncherItemModel_Table.apkPkg.eq("com.ss.android.article.news"))
                .execute();
//        Where<LauncherItemModel> where = new Delete().from(LauncherItemModel.class).where(LauncherItemModel_Table.apkPkg.eq("com.ss.android.article.news"));
//        where.execute();
    }
}
