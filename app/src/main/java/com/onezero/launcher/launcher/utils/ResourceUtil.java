package com.onezero.launcher.launcher.utils;

import android.content.Context;
import android.util.Log;

import com.onezero.launcher.launcher.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lizeiwei on 2018/7/31.
 */

public class ResourceUtil {
    public static String getRawResourceString(Context context, String fileName, String pkgName) {
        int treeId = context.getResources().getIdentifier(fileName.toLowerCase(), "raw", pkgName);
        if (treeId == 0) {
            treeId = R.raw.rk3288_default_config;
        }
        InputStream inputStream = context.getResources().openRawResource(treeId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String lineStr;
        StringBuilder result = new StringBuilder();
        try {
            while ((lineStr = bufferedReader.readLine()) != null) {
                result.append(lineStr);
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
