package com.onezero.launcher.launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.callback.RecyclerViewClickListener;
import com.onezero.launcher.launcher.utils.FragmentHelper;

import java.util.List;

public class LauncherRecyclerViewAdapter extends RecyclerView.Adapter<AppInfoViewHolder> {
    private Context mContext;
    private RecyclerViewClickListener listener;
    private List<AppInfo> list;

    public LauncherRecyclerViewAdapter(Context context, List<AppInfo> list) {
        this.list = list;
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
    public void onBindViewHolder(final AppInfoViewHolder holder, final int position) {
        final AppInfo appInfo = list.get(position);
        final boolean isSystemApp = appInfo.isSystemApp();
        holder.icon.setImageDrawable(appInfo.getAppIconId());
        holder.label.setText(appInfo.getAppLabel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appInfo.isRemoveable()) {
                    listener.onItemClick(appInfo);
                }
                FragmentHelper.resetRemoveIcon(list);
                notifyItemChanged(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isSystemApp) {
                    Toast.makeText(mContext, R.string.can_not_remove_system_app, Toast.LENGTH_LONG).show();
                    return true;
                }
                appInfo.setRemoveable(true);
                notifyItemChanged(position);
                return true;
            }
        });
        boolean visible = !isSystemApp && appInfo.isRemoveable();
        holder.removeIcon.setVisibility(visible ? View.VISIBLE : View.GONE);
        holder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRemoveClick(appInfo);
                appInfo.setRemoveable(false);
                notifyItemChanged(position);
            }
        });

    }

//    public static void resetRemoveIcon(List<AppInfo> list) {
//        for (int i = 0; i < list.size(); i++) {
//            list.get(i).setRemoveable(false);
//        }
//    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
