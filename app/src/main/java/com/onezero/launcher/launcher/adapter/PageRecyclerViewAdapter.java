package com.onezero.launcher.launcher.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.callback.RecyclerViewClickListener;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemRemoveClickEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PageRecyclerViewAdapter extends RecyclerView.Adapter<AppInfoViewHolder> {
    private Context mContext;
    private RecyclerViewClickListener listener;
    private List<AppInfo> dataList;
    private int firstPageRows;
    private int fullPageRows;
    private int columns;
    private int hideCounts;

    public PageRecyclerViewAdapter(Context context) {
        this.mContext = context;
        initData();
    }

    private void initData() {
        firstPageRows = mContext.getResources().getInteger(R.integer.first_page_rows);
        columns = mContext.getResources().getInteger(R.integer.per_page_columens);
        fullPageRows = mContext.getResources().getInteger(R.integer.full_page_rows);
        hideCounts = (fullPageRows - firstPageRows) * columns;
    }

    public void setAppinfoData(List<AppInfo> list) {
        this.dataList = list;
        notifyDataSetChanged();
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
        Log.d("tag", "=====onBindViewHolder====position:" + position);
        if (position <= (hideCounts - 1)) {
            holder.itemLayout.setVisibility(View.GONE);
        } else if (position > (hideCounts - 1)) {
            final AppInfo appInfo = dataList.get(position - hideCounts);
            final boolean isSystemApp = appInfo.isSystemApp();
            holder.icon.setImageDrawable(appInfo.getAppIconId());
            holder.label.setText(appInfo.getAppLabel());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new OnAppItemClickEvent(appInfo));
                    if (!appInfo.isRemoveable() && listener != null) {
//                        listener.onItemClick(appInfo);
                    }
//                    FragmentHelper.resetRemoveIcon(dataList);
//                    notifyItemChanged(position);
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
                    notifyDataSetChanged();
                    return true;
                }
            });
            boolean visible = !isSystemApp && appInfo.isRemoveable();
            holder.removeIcon.setVisibility(visible ? View.VISIBLE : View.GONE);
            holder.removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new OnAppItemRemoveClickEvent(appInfo));
                    appInfo.setRemoveable(false);
                    notifyItemChanged(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : (dataList.size() + hideCounts);
    }
}
