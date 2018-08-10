package com.onezero.launcher.launcher.activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.onezero.launcher.launcher.DateReceiver;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.BottomRecyclerViewAdapter;
import com.onezero.launcher.launcher.adapter.PageRecyclerViewAdapter;
import com.onezero.launcher.launcher.appInfo.AppChangeReceiver;
import com.onezero.launcher.launcher.appInfo.AppInfo;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.appInfo.ApplicationHelper;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemRemoveClickEvent;
import com.onezero.launcher.launcher.event.PackageChangedEvent;
import com.onezero.launcher.launcher.helper.HorizontalPageLayoutManager;
import com.onezero.launcher.launcher.helper.PagingScrollHelper;
import com.onezero.launcher.launcher.presenter.LauncherPresenter;
import com.onezero.launcher.launcher.utils.DeviceConfig;
import com.onezero.launcher.launcher.utils.StringUtils;
import com.onezero.launcher.launcher.view.IAppContentView;
import com.onezero.launcher.launcher.view.ITimeView;
import com.onezero.launcher.launcher.view.PageIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class Launcher extends AppCompatActivity implements PagingScrollHelper.onPageChangeListener, ITimeView, IAppContentView {

    private RecyclerView appContent;
    private PageIndicatorView pageIndicator;
    private RecyclerView bottomAppContent;
    private FrameLayout timeLayout;
    private TextView weekText;
    private TextView dateText;
    private LauncherPresenter presenter;
    private DateReceiver dateReceiver;
    private IntentFilter dateChangeFilter;
    private List<String> excludeAppsConfigs;
    private PageRecyclerViewAdapter appsContentAdapter;
    private int currentPosition = 0;
    private int fullPageRows;
    private int columns;
    private PagingScrollHelper pagingScrollHelper;
    private List<String> bottomAppsConfigs;
    private AppChangeReceiver appChangeReceiver;
    private IntentFilter appChangeFilter;
    private List<AppInfo> appDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        //设置系统桌面为背景
        Drawable wallPaper = WallpaperManager.getInstance(this).getDrawable();
        this.getWindow().setBackgroundDrawable(wallPaper);
        presenter = new LauncherPresenter(this, this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.updateTime();
        initData();
        initPagingScrollHelper();
        initReceiver();
    }

    private void initData() {
        excludeAppsConfigs = DeviceConfig.getInstance(this).getExcludeAppsConfigs();
        bottomAppsConfigs = DeviceConfig.getInstance(this).getBottomAppsConfigs();
        presenter.setAppContentView(getPackageManager(), excludeAppsConfigs);
        presenter.setBottomContentView(getPackageManager(), bottomAppsConfigs);
    }

    private void initPagingScrollHelper() {
        pagingScrollHelper = new PagingScrollHelper();
        pagingScrollHelper.setUpRecycleView(appContent);
        pagingScrollHelper.setOnPageChangeListener(this);
        pagingScrollHelper.scrollToPosition(currentPosition);
    }

    private void initViews() {
        timeLayout = findViewById(R.id.time_layout);
        weekText = timeLayout.findViewById(R.id.launcher_week_text);
        dateText = timeLayout.findViewById(R.id.launcher_date_text);

        appContent = findViewById(R.id.app_content_view);
        fullPageRows = getResources().getInteger(R.integer.full_page_rows);
        columns = getResources().getInteger(R.integer.per_page_columens);

        pageIndicator = findViewById(R.id.page_indicator);
        bottomAppContent = findViewById(R.id.launcher_bottom_area);
    }

    @Override
    public void onPageChange(int index) {
        currentPosition = index;
        if (index == 0) {
            timeLayout.setVisibility(View.VISIBLE);
        } else {
            timeLayout.setVisibility(View.GONE);
        }
        pageIndicator.setSelectedPage(index);
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
    protected void onPause() {
        super.onPause();
        if (dateReceiver != null) {
            unregisterReceiver(dateReceiver);
            dateReceiver = null;
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initReceiver() {
        if (dateReceiver == null) {
            dateReceiver = new DateReceiver(presenter);
        }

        if (dateChangeFilter == null) {
            dateChangeFilter = new IntentFilter();
            dateChangeFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            dateChangeFilter.addAction(Intent.ACTION_DATE_CHANGED);
        }

        if (appChangeReceiver == null) {
            appChangeReceiver = new AppChangeReceiver();
        }

        if (appChangeFilter == null) {
            appChangeFilter = new IntentFilter();
            appChangeFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            appChangeFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            appChangeFilter.addDataScheme("package");
        }
        registerReceiver(dateReceiver, dateChangeFilter);
        registerReceiver(appChangeReceiver, appChangeFilter);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * start layout the content of all apps
     * @param list
     */
    @Override
    public void layoutAllAppsContent(List<AppInfo> list) {
        this.appDataList = list;
        appsContentAdapter = new PageRecyclerViewAdapter(Launcher.this.getApplicationContext());
        if (list != null) {
            appsContentAdapter.setAppinfoData(list);
        }
        appContent.setAdapter(appsContentAdapter);
        int size = list.size();
        int firstPageRows = getResources().getInteger(R.integer.first_page_rows);
        int firstPageAppCount = columns * firstPageRows;
        int indicatorCount;
        if (size <= firstPageAppCount) {
            indicatorCount = 1;
        } else if ((size - firstPageAppCount) % (fullPageRows * columns) == 0) {
            indicatorCount = (size - firstPageAppCount) / (fullPageRows * columns);
        } else {
            indicatorCount = (size - firstPageAppCount) / (fullPageRows * columns) + 1 + 1;
        }
        HorizontalPageLayoutManager layoutManager = new HorizontalPageLayoutManager(fullPageRows, columns);
        appContent.setLayoutManager(layoutManager);
        appContent.setHorizontalScrollBarEnabled(true);
        pageIndicator.initIndicator(indicatorCount);
    }

    @Override
    public void layoutBottomAppContent(List<AppInfo> list) {
        BottomRecyclerViewAdapter bottomRecyclerViewAdapter = new BottomRecyclerViewAdapter(this, list);
        bottomAppContent.setAdapter(bottomRecyclerViewAdapter);
        bottomAppContent.setLayoutManager(new GridLayoutManager(this, list.size()));
    }


    @Subscribe
    public void onItemClick(OnAppItemClickEvent event) {
        ApplicationHelper.performStartApp(this, event.getInfo());
    }

    @Subscribe
    public void onItemRemoveClick(OnAppItemRemoveClickEvent event) {
        ApplicationHelper.performUninstallApp(this, event.getInfo());
    }

    @Subscribe
    public void onPackageChanged(PackageChangedEvent event) {
//        calculateIndicators();
        if (event.isNewAdd()) {
            appDataList.add(AppInfoUtils.getAppInfoByPkgName(getPackageManager(), event.getPkgName()));
            appsContentAdapter.notifyDataSetChanged();
        }
    }
}
