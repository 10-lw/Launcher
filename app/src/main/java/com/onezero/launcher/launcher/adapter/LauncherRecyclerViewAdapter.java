package com.onezero.launcher.launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.callback.RecyclerViewClickListener;

import java.util.List;

public class LauncherRecyclerViewAdapter extends RecyclerView.Adapter<AppInfoViewHolder> {
    private Context mContext;
    private RecyclerViewClickListener listener;
    private List<AppInfo> list;

    public LauncherRecyclerViewAdapter(Context context, List<AppInfo> list) {
        this.list = list;
        Log.d("tag", "=====LauncherRecyclerViewAdapter======" + list.size());
        this.mContext = context;
    }

    public void setOnClickListener(RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_info_item, parent, false);
        return new AppInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder holder, int position) {
        final AppInfo appInfo = list.get(position);
        holder.icon.setImageDrawable(appInfo.getAppIconId());
        holder.label.setText(appInfo.getAppLabel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(appInfo);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.OnLongClick(appInfo);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("tag", "=====getItemCount======" + list.size());
        return list == null ? 0 : list.size();
    }
}
