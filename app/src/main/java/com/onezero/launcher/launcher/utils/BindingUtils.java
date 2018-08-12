package com.onezero.launcher.launcher.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class BindingUtils {
    @BindingAdapter({"bind:loadDrawable"})
    public static void loadImage(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }
}
