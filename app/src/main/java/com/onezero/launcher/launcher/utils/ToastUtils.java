package com.onezero.launcher.launcher.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast = null;
    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }

        mToast.show();
    }

    public static void showToast(Context context, int strId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, strId, Toast.LENGTH_LONG);
        } else {
            mToast.setText(strId);
            mToast.setDuration(Toast.LENGTH_LONG);
        }

        mToast.show();
    }
}