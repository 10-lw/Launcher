package com.onezero.launcher.launcher.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.onezero.launcher.launcher.api.PostResult;
import com.onezero.launcher.launcher.model.AppInfo;
import com.onezero.launcher.launcher.model.LauncherItemModel;
import com.onezero.launcher.launcher.model.LauncherItemModel_Table;
import com.onezero.launcher.launcher.model.PreAppInfo;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AppListHelper {
    List<String> bigTypes = new ArrayList<>();
    private static AppListHelper helperInstance;
    public static final String DATA_LIST_TAG = "data";

    private AppListHelper() {
    }

    public static AppListHelper getInstance() {
        if (helperInstance == null) {
            helperInstance = new AppListHelper();
        }
        return helperInstance;
    }


    @SuppressLint("CheckResult")
    public List<AppInfo> getAppInfoList(Context context) {
        List<AppInfo> list = new ArrayList<>();
        Call<PostResult> resultCall = HttpUtils.createAppInfoListService(context).getPreAppInfoList();
        try {
            Response<PostResult> response = resultCall.execute();
            PostResult postResult = response.body();
            if (postResult == null || !postResult.isStatus()) {
                return list;
            }
            String str = "{\"status\":" + String.valueOf(postResult.isStatus()) + ",\"message\":" + postResult.getMessage() + "}";
//            Log.w("tag", "?????getAppInfoList: 2222 str:" + str);

            Gson gson = new Gson();
            PreAppInfo preAppinfo = gson.fromJson(str, PreAppInfo.class);
            if (preAppinfo.isStatus()) {
                List<PreAppInfo.MessageBean> preAppList = preAppinfo.getMessage();
                if (preAppList != null && preAppList.size() != 0) {
                    for (int i = 0; i < preAppList.size(); i++) {
                        PreAppInfo.MessageBean preApp = preAppList.get(i);
                        AppInfo appInfo = new AppInfo();
                        appInfo.setPkgName(preApp.getPackageX());
                        appInfo.setAppLabel(preApp.getName());
                        appInfo.setSystemApp(false);
                        appInfo.setRemoveable(true);
                        appInfo.setVisiable(true);
                        appInfo.setVirtuallApp(true);
                        appInfo.setVirtualAppId(preApp.getId());
                        appInfo.setDownloadPath(preApp.getPath());

                        Where<LauncherItemModel> where = new Select()
                                .from(LauncherItemModel.class)
                                .where(LauncherItemModel_Table.apkPkg.eq(preApp.getPackageX()));
                        LauncherItemModel model = where.querySingle();
                        if (model != null) {
                            appInfo.setPosition(model.position);
                        } else {
                            appInfo.setPosition(Integer.MAX_VALUE - i);
                        }

                        list.add(appInfo);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


    public static Drawable bitmapToDrawable(Bitmap bitmap){
        //BitmapDrawable 是 Drawable的派生类
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }


}
