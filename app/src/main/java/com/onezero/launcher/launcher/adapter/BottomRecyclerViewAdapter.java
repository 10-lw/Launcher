package com.onezero.launcher.launcher.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onezero.launcher.launcher.BR;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.ApplicationHelper;
import com.onezero.launcher.launcher.databinding.AppInfoItemBinding;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BottomRecyclerViewAdapter extends RecyclerView.Adapter<AppInfoViewHolder> {
    private List<AppInfo> dataList;
    private Context mContext;

    public BottomRecyclerViewAdapter(Context context, List<AppInfo> list) {
        this.dataList = list;
        this.mContext = context;
    }

    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppInfoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.app_info_item, parent, false);
        return new AppInfoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder holder, int position) {
        final AppInfo appInfo = dataList.get(position);
        holder.getDatabinding().setVariable(BR.info, appInfo);
//        holder.icon.setImageDrawable(appInfo.getAppIconId());
////        holder.label.setText(appInfo.getAppLabel());
        holder.label.setVisibility(View.GONE);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationHelper.performStartApp(mContext, appInfo);
                EventBus.getDefault().post(new OnAppItemClickEvent(appInfo));
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}
