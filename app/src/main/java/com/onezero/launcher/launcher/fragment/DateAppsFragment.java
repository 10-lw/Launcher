package com.onezero.launcher.launcher.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.onezero.launcher.launcher.DateReceiver;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.LauncherRecyclerViewAdapter;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.callback.RecyclerViewClickListener;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemRemoveClickEvent;
import com.onezero.launcher.launcher.presenter.LauncherPresenter;
import com.onezero.launcher.launcher.utils.StringUtils;
import com.onezero.launcher.launcher.view.ITimeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class DateAppsFragment extends BaseAppFragment implements ITimeView {
    private TextView weekText;
    private TextView dateText;
    private LauncherPresenter presenter;
    private DateReceiver dateReceiver;
    private List<AppInfo> list;
    private RecyclerView firstRecyclerView;
    private IntentFilter filter;

    public DateAppsFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_launcher_date_apps;
    }

    @Override
    protected void initViews(View view) {
        weekText = view.findViewById(R.id.launcher_week_text);
        dateText = view.findViewById(R.id.launcher_date_text);
        firstRecyclerView = view.findViewById(R.id.launcher_first_page_recycler_view);
    }

    @Override
    protected void initData() {
        presenter = new LauncherPresenter(this);
        LauncherRecyclerViewAdapter adapter = new LauncherRecyclerViewAdapter(this.getContext(), list);
        firstRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter.setOnClickListener(new RecyclerViewClickListener() {
            @Override
            public void onItemClick(AppInfo info) {
                EventBus.getDefault().post(new OnAppItemClickEvent(info));
            }

            @Override
            public void onRemoveClick(AppInfo info) {
                EventBus.getDefault().post(new OnAppItemRemoveClickEvent(info));
            }
        });
        firstRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateTime();
    }

    @Override
    public void onStart() {
        super.onStart();
        initReceiver();
    }

    private void initReceiver() {
        if (dateReceiver == null) {
            dateReceiver = new DateReceiver(presenter);
        }

        if (filter == null) {
            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_DATE_CHANGED);
        }
        getContext().registerReceiver(dateReceiver, filter);
    }

    @Override
    public void updateDate(String date, String week) {
        if (!StringUtils.isNullOrEmpty(date)) {
            dateText.setText(date);
        }
        if (!StringUtils.isNullOrEmpty(week)) {
            weekText.setText(week);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(dateReceiver);
    }

    @Override
    public void setAppInfos(List<AppInfo> list) {
        this.list = list;
    }

    @Override
    public List<AppInfo> getAppListInfo() {
        return list;
    }
}
