package com.onezero.launcher.launcher.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.databinding.AppInfoItemBinding;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemRemoveClickEvent;
import com.onezero.launcher.launcher.pageRecyclerView.PageRecyclerView;
import com.onezero.launcher.launcher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AllAppsPageAdapter extends PageRecyclerView.PageAdapter<AppInfoViewHolder> {
    private Context mContext;
    private int fullPageRows;
    private int columns;
    private int hideCounts;
    private List<AppInfo> list;
    private boolean enterRemoveMode;

    /**
     * @param context
     * @param fullPageRows
     * @param columns
     * @param list
     * @param hideCounts   first page hide items
     */
    public AllAppsPageAdapter(Context context, int fullPageRows, int columns, List<AppInfo> list, int hideCounts) {
        this.mContext = context;
        this.fullPageRows = fullPageRows;
        this.columns = columns;
        this.list = list;
        this.hideCounts = hideCounts;
    }

    public void setDataList(List<AppInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getRowCount() {
        return fullPageRows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : (list.size());
    }

    @Override
    public AppInfoViewHolder onPageCreateViewHolder(ViewGroup parent, int viewType) {
        AppInfoItemBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.app_info_item, parent, false);
        return new AppInfoViewHolder(dataBinding);
    }

    @Override
    public void onPageBindViewHolder(AppInfoViewHolder holder, final int position) {
            final AppInfo appInfo = list.get(position);
            final boolean isSystemApp = appInfo.isSystemApp();
            AppInfoItemBinding binding = holder.getDatabinding();
            binding.setVariable(BR.info, appInfo);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(enterRemoveMode) {
                        resetState();
                        enterRemoveMode = false;
                    }
                    EventBus.getDefault().post(new OnAppItemClickEvent(appInfo));
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (isSystemApp) {
                        ToastUtils.showToast(mContext, R.string.can_not_remove_system_app);
                        return true;
                    }
                    enterRemoveMode = true;
                    appInfo.setRemoveable(true);
                    return true;
                }
            });

            holder.removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterRemoveMode = false;
                    EventBus.getDefault().post(new OnAppItemRemoveClickEvent(appInfo));
                    appInfo.setRemoveable(false);
                    notifyItemChanged(position);
                }
            });
    }

    public boolean resetState() {
        if (enterRemoveMode) {
            AppInfoUtils.resetAllAppRemoveableState(list);
            return true;
        } else {
            return false;
        }
    }
}
