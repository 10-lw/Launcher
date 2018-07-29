package com.onezero.launcher.launcher.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.onezero.launcher.launcher.DateReceiver;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.LauncherRecyclerViewAdapter;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.callback.RecyclerViewClickListener;
import com.onezero.launcher.launcher.presenter.LauncherPresenter;
import com.onezero.launcher.launcher.utils.StringUtils;
import com.onezero.launcher.launcher.view.ITimeView;

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
            public void OnItemClick(AppInfo info) {
                Log.d("tag", "====OnItemClick==="+info.getAppLabel());
            }

            @Override
            public void OnLongClick(AppInfo info) {
                Log.d("tag", "====OnLongClick==="+info.getAppLabel());
            }
        });
        firstRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateTime();
        initReceiver();
    }

    private void initReceiver() {
        if (dateReceiver == null) {
            dateReceiver = new DateReceiver(presenter);
            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_DATE_CHANGED);
        }
        getActivity().registerReceiver(dateReceiver, filter);
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
        Log.d("tag", "======app info  date apps=="+list.size());
    }

    @Override
    public List<AppInfo> getAppListInfo() {
        return list;
    }
}
