package com.onezero.launcher.launcher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.databinding.AppInfoItemBinding;

public class AppInfoViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout itemLayout;
    private AppInfoItemBinding databinding;

    public ImageView removeIcon;
    public ImageView installIcon;
    public TextView label;

    public AppInfoViewHolder(AppInfoItemBinding databinding) {
        super(databinding.getRoot());
        this.databinding = databinding;
        removeIcon = databinding.getRoot().findViewById(R.id.app_info_remove);
        label = databinding.getRoot().findViewById(R.id.launcher_app_label);
        itemLayout = databinding.getRoot().findViewById(R.id.item_layout);
        installIcon = databinding.getRoot().findViewById(R.id.app_info_install);
    }

    public AppInfoItemBinding getDatabinding() {
        return databinding;
    }
}
