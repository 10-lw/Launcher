package com.onezero.launcher.launcher.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class ArmBkUtils {

    private static final String PKG = "com.yunarm.appstore";
    private static final String SERVICE = "com.yunarm.appstore.services.ArmBkService";

    public static final String PATH = "path";
    public static final String PACKAGE = "package";
    public static final String VERSION_CODE = "versionCode";
    public static final String DOWN_PATH = "downPath";

    public static final String IP = "ip";
    public static final String PORT = "port";
    public static final String USER_NAME = "userName";
    public static final String PWD = "pwd";

    /**
     * 特定地址安装apk
     *
     * @param path    *必传  应用提取路径
     * @param pkg     *必传   应用包名
     * @param ip      *必传   服务器地址
     * @param port    *必传  服务器端口
     * @param name    *必传  用户名
     * @param pwd     *必传   密码
     * @param version 应用版本号  ps:小于1时，强制更新
     * @param save    保存路径   格式：xxx/    ps:默认保存在根目录下 apk/
     */
    public static void installApk(Context context, String path, String pkg, long version, String save, String ip, int port, String name, String pwd) {
        Intent intent = base(path, save);
        context.startService(configs(apk(intent, pkg, version), ip, port, name, pwd));
        Log.d("ArmBkServiceUtils", "installApk config...");
    }

    /**
     * 内部下载安装apk
     *
     * @param path    *必传 应用提取路径
     * @param pkg     *必传   应用包名
     * @param version 应用版本号  ps:小于1时，强制更新
     * @param save    保存路径   格式：xxx/    ps:默认保存在根目录下 apk/
     */
    public static void installApk(Context context, String path, String pkg, long version, String save) {
        Intent intent = base(path, save);
        context.startService(apk(intent, pkg, version));
        Log.d("ArmBkServiceUtils", "installApk...");
    }

    /**
     * 特定地址下载文件
     *
     * @param path *必传 文件提取路径
     * @param ip   *必传   服务器地址
     * @param port *必传  服务器端口
     * @param name *必传  用户名
     * @param pwd  *必传   密码
     * @param save 保存路径   格式：xxx/    ps:默认保存在根目录下 file/
     */
    public static void downFile(Context context, String path, String save, String ip, int port, String name, String pwd) {
        Intent intent = base(path, save);
        context.startService(configs(intent, ip, port, name, pwd));
        Log.d("ArmBkServiceUtils", "downFile config...");
    }

    /**
     * 内部下载文件
     *
     * @param path *必传 文件提取路径
     * @param save 保存路径   格式：xxx/    ps:默认保存在根目录下 file/
     */
    public static void downFile(Context context, String path, String save) {
        Intent intent = base(path, save);
        context.startService(intent);
        Log.d("ArmBkServiceUtils", "downFile ...");
    }

    private static Intent base(String path, String save) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PKG, SERVICE);
        intent.setComponent(componentName);
        intent.putExtra(PATH, path);
        return save(intent, save);
    }

    private static Intent save(Intent intent, String save) {
        if (!TextUtils.isEmpty(save))
            intent.putExtra(DOWN_PATH, save);
        return intent;
    }

    private static Intent apk(Intent intent, String pkg, long version) {
        intent.putExtra(PACKAGE, pkg);
        intent.putExtra(VERSION_CODE, version);
        return intent;
    }

    private static Intent configs(Intent intent, String ip, int port, String name, String pwd) {
        intent.putExtra(IP, ip);
        intent.putExtra(PORT, port);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(PWD, pwd);
        return intent;
    }

}
