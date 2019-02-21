package com.onezero.launcher.launcher.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtil {
    public static String getTextFromClip(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //判断剪切版时候有内容
        if (!clipboardManager.hasPrimaryClip()) {
            return "";
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        //获取 text
        String text = "";
        if (clipData != null) {
            text = clipData.getItemAt(0).getText().toString();
        }
        return text;
    }

    public static void putTextIntoClip(Context context, String clipboardText) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("Label", clipboardText);
        //添加ClipData对象到剪切板中
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
        }
    }
}
