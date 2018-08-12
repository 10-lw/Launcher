package com.onezero.launcher.launcher.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.databinding.AppInfoItemBinding;

public class AppInfoViewHolder extends RecyclerView.ViewHolder {
    private AppInfoItemBinding databinding;

    public ImageView removeIcon;
    public TextView label;

    public AppInfoViewHolder(AppInfoItemBinding databinding) {
        super(databinding.getRoot());
        this.databinding = databinding;
        removeIcon = databinding.getRoot().findViewById(R.id.app_info_remove);
        label = databinding.getRoot().findViewById(R.id.launcher_app_label);
    }

    public AppInfoItemBinding getDatabinding() {
        return databinding;
    }
}
