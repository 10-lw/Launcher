package com.onezero.launcher.launcher.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.http.ApplicationConstant;
import com.squareup.picasso.Picasso;

public class BindingUtils {
    @BindingAdapter({"bind:loadDrawable"})
    public static void loadImage(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("loadImage")
    public static void loadImageByIconID(final ImageView view, String icon) {
        if (icon == null) {
            return;
        }
        final String url = ApplicationConstant.HOST + "/app/" + icon + "/icon.png";
        Picasso.with(view.getContext()).load(url).placeholder(R.mipmap.app_icon).error(R.mipmap.app_icon).into(view);
    }
}
