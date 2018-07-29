package com.onezero.launcher.launcher.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.LauncherRecyclerViewAdapter;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.callback.RecyclerViewClickListener;

import java.util.List;

public class AllAppsFragment extends BaseAppFragment {

    private RecyclerView recyclerView;

    public AllAppsFragment() {
    }

    private List<AppInfo> list;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_per_page_layput;
    }

    @Override
    protected void initViews(View view) {
        recyclerView = view.findViewById(R.id.launcher_apps_recycler_view);
    }

    @Override
    protected void initData() {
        LauncherRecyclerViewAdapter adapter = new LauncherRecyclerViewAdapter(getContext(), list);
        adapter.setOnClickListener(new RecyclerViewClickListener() {
            @Override
            public void OnItemClick(AppInfo info) {

            }

            @Override
            public void OnLongClick(AppInfo info) {

            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setAppInfos(List<AppInfo> list) {
        this.list = list;
        Log.d("tag", "===all apps size=="+list.size());
    }

    @Override
    public List<AppInfo> getAppListInfo() {
        return list;
    }
}
