package com.onezero.launcher.launcher.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.model.AppInfo;
import com.onezero.launcher.launcher.appInfo.ApplicationHelper;
import com.onezero.launcher.launcher.databinding.AppInfoItemBinding;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemRemoveClickEvent;
import com.onezero.launcher.launcher.model.LauncherItemModel;
import com.onezero.launcher.launcher.pageRecyclerView.PageRecyclerView;
import com.onezero.launcher.launcher.utils.ArmBkUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

public class AllAppsPageAdapter extends PageRecyclerView.PageAdapter<AppInfoViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private int fullPageRows;
    private int columns;
    private int hideCounts;
    private List<AppInfo> list = null;
    private boolean enterRemoveMode;
    private AppInfoViewHolder longClickItem;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onPageBindViewHolder(final AppInfoViewHolder holder, final int position) {
            final AppInfo appInfo = list.get(position);
            saveToDb(appInfo, position);
            final boolean isSystemApp = appInfo.isSystemApp();
            AppInfoItemBinding binding = holder.getDatabinding();
            binding.setVariable(BR.info, appInfo);

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(enterRemoveMode) {
                        resetState();
                        enterRemoveMode = false;
                    }
                    if (appInfo.isVirtuallApp()) {
                        return;
                    }
                    ApplicationHelper.performStartApp(mContext, appInfo);
                    EventBus.getDefault().post(new OnAppItemClickEvent(appInfo));
                }
            });

            holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (isSystemApp || appInfo.isVirtuallApp()) {
//                        ToastUtils.showToast(mContext, R.string.can_not_remove_system_app);
                        return true;
                    }
                    enterRemoveMode = true;
                    longClickItem = holder;
                    holder.removeIcon.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            holder.removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterRemoveMode = false;
                    EventBus.getDefault().post(new OnAppItemRemoveClickEvent(appInfo));
                    longClickItem.removeIcon.setVisibility(View.GONE);
                    notifyItemChanged(position);
                }
            });

            if (appInfo.isVirtuallApp()) {
                holder.installIcon.setVisibility(View.VISIBLE);
            }

            holder.installIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appInfo.setAppLabel("正在安装...");
                    ArmBkUtils.installApk(mContext, "/app/"+appInfo.getDownloadPath(), appInfo.getPkgName(),12, "");
                }
            });
    }

    private void saveToDb(AppInfo appInfo, int position) {
        LauncherItemModel model = new LauncherItemModel();
        model.appLabel = appInfo.getAppLabel();
        model.apkPkg = appInfo.getPkgName();
        model.position = position;
        model.save();
    }

    public boolean resetState() {
        if (enterRemoveMode) {
            longClickItem.removeIcon.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (toPosition <= hideCounts -1) {
            toPosition = hideCounts;
        } else if(toPosition >= getDataCount()) {
            toPosition = getDataCount() -1;
        }

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                exchange(list, i, i+1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                int j = i - 1;
                exchange(list, i, j);
            }

        }
        notifyItemMoved(fromPosition, toPosition);
    }

    private void exchange(List<AppInfo> list, int fromPosition, int toPosition) {
        AppInfo fromInfo = list.get(fromPosition);
        AppInfo toInfo = list.get(toPosition);
        LauncherItemModel model = new LauncherItemModel();
        model.apkPkg = fromInfo.getPkgName();
        model.position = toPosition;
        model.appLabel= fromInfo.getAppLabel();
        fromInfo.setPosition(toPosition);
        model.save();

        model.apkPkg = toInfo.getPkgName();
        model.position = fromPosition;
        model.appLabel= toInfo.getAppLabel();
        toInfo.setPosition(fromPosition);
        model.save();

        Collections.swap(list, fromPosition, toPosition);
    }
}
