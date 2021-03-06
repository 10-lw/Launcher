package com.onezero.launcher.launcher.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.onezero.launcher.launcher.DateReceiver;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.adapter.AllAppsPageAdapter;
import com.onezero.launcher.launcher.adapter.BottomRecyclerViewAdapter;
import com.onezero.launcher.launcher.adapter.SimpleItemTouchHelperCallback;
import com.onezero.launcher.launcher.appInfo.AppInfoUtils;
import com.onezero.launcher.launcher.appInfo.ApplicationHelper;
import com.onezero.launcher.launcher.event.OnAppItemClickEvent;
import com.onezero.launcher.launcher.event.OnAppItemRemoveClickEvent;
import com.onezero.launcher.launcher.event.PackageChangedEvent;
import com.onezero.launcher.launcher.http.ApplicationConstant;
import com.onezero.launcher.launcher.model.AppInfo;
import com.onezero.launcher.launcher.model.LauncherItemModel;
import com.onezero.launcher.launcher.model.LauncherItemModel_Table;
import com.onezero.launcher.launcher.pageRecyclerView.DisableScrollGridManager;
import com.onezero.launcher.launcher.pageRecyclerView.PageRecyclerView;
import com.onezero.launcher.launcher.presenter.LauncherPresenter;
import com.onezero.launcher.launcher.utils.DeviceConfig;
import com.onezero.launcher.launcher.utils.StringUtils;
import com.onezero.launcher.launcher.utils.ToastUtils;
import com.onezero.launcher.launcher.view.IAppContentView;
import com.onezero.launcher.launcher.view.ITimeView;
import com.onezero.launcher.launcher.view.PageIndicatorView;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.william.androidsdk.widget.armdialog.manager.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Launcher extends AppCompatActivity implements ITimeView, IAppContentView, PageRecyclerView.OnPagingListener, PageRecyclerView.OnTouchActionUpListener {

    private String TAG = "Launcher";
    private PageRecyclerView appContent;
    private PageIndicatorView pageIndicator;
    private RecyclerView bottomAppContent;
    private FrameLayout timeLayout;
    private TextView weekText;
    private TextView dateText;
    private LauncherPresenter presenter;
    private DateReceiver dateReceiver;
    private IntentFilter dateChangeFilter;
    private List<String> excludeAppsConfigs;
    private int currentPage = 0;
    private int fullPageRows;
    private int columns = 0;
    private List<String> bottomAppsConfigs;
    private List<AppInfo> appDataList = new ArrayList<>();
    private int firstPageRows;
    private int hideCounts;
    private AllAppsPageAdapter appsContentAdapter;
    private boolean startApp = false;
    private ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        //设置系统桌面为背景
        getWindow().setBackgroundDrawableResource(R.mipmap.wallpaper);
        presenter = new LauncherPresenter(this, this);
        initViews();
    }

    private void loadData() {
        DialogUtil.createLoadingDialog(this,"正在加载...");
        appDataList.clear();
        AppInfoUtils.getDefaultDataList(appDataList, hideCounts);
        presenter.setAppContentView(getApplicationContext(), getPackageManager(), excludeAppsConfigs, hideCounts);
        presenter.setBottomContentView(getPackageManager(), bottomAppsConfigs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initConfigData();
        loadData();
        presenter.updateTime();
        initReceiver();
    }

    private void initConfigData() {
        fullPageRows = getResources().getInteger(R.integer.full_page_rows);
        columns = getResources().getInteger(R.integer.per_page_columens);
        firstPageRows = getResources().getInteger(R.integer.first_page_rows);
        hideCounts = (fullPageRows - firstPageRows) * columns;
        excludeAppsConfigs = DeviceConfig.getInstance(this).getExcludeAppsConfigs();
        bottomAppsConfigs = DeviceConfig.getInstance(this).getBottomAppsConfigs();
    }

    private void initViews() {
        timeLayout = findViewById(R.id.time_layout);
        weekText = timeLayout.findViewById(R.id.launcher_week_text);
        dateText = timeLayout.findViewById(R.id.launcher_date_text);

        appContent = findViewById(R.id.app_content_view);
        pageIndicator = findViewById(R.id.page_indicator);
        bottomAppContent = findViewById(R.id.launcher_bottom_area);

        appContent.setOnPagingListener(this);
        appContent.setOnTouchActionUpListener(this);
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

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (appsContentAdapter != null && !appsContentAdapter.resetState()) {
            ToastUtils.showToast(this, R.string.have_been_to_desktop);
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
            dateChangeFilter.addAction(ApplicationConstant.DOWNLOAD_ACTION);
            dateChangeFilter.addAction(ApplicationConstant.INSTALL_APP_ERROR_ACTION);
        }
        registerReceiver(dateReceiver, dateChangeFilter);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * start layout the content of all apps
     *
     * @param list
     */
    @Override
    public void layoutAllAppsContent(final List<AppInfo> list) {
        appDataList.addAll(hideCounts, list);
        DisableScrollGridManager manager = new DisableScrollGridManager(Launcher.this);
        appContent.setLayoutManager(manager);
        Collections.sort(appDataList);
        calculateIndicators(appDataList);
        appsContentAdapter = new AllAppsPageAdapter(this, fullPageRows, columns, appDataList, hideCounts);
        appContent.setAdapter(appsContentAdapter);
        appContent.gotoPage(currentPage);
        appContent.setHasFixedSize(true);

        SimpleItemTouchHelperCallback helperCallback = new SimpleItemTouchHelperCallback(appsContentAdapter, this);
        helperCallback.setTouchListener(appContent);
        //用Callback构造ItemtouchHelper
        touchHelper = new ItemTouchHelper(helperCallback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(appContent);
        presenter.setVirtualApp(getApplicationContext(), getPackageManager(), excludeAppsConfigs, hideCounts);
    }

    @Override
    public void layoutVirtualApps(List<AppInfo> list) {
        if (appsContentAdapter == null || appDataList == null || list.size() == 0) {
            DialogUtil.closeLoadingDialog(this);
            return;
        }
        appDataList.addAll(appDataList.size(), list);
        Collections.sort(appDataList);
        calculateIndicators(appDataList);
        appsContentAdapter.setDataList(appDataList);
        DialogUtil.closeLoadingDialog(this);
    }

    private int calculateIndicators(List<AppInfo> list) {
        int size = list.size();
        int firstPageRows = getResources().getInteger(R.integer.first_page_rows);
        int firstPageAppCount = columns * firstPageRows;
        int indicatorCount;
        if (size  % (fullPageRows * columns) == 0) {
            indicatorCount = size / (fullPageRows * columns);
        } else {
            indicatorCount = size / (fullPageRows * columns) + 1;
        }
        pageIndicator.initIndicator(indicatorCount);
        return indicatorCount;
    }

    @Override
    public void layoutBottomAppContent(List<AppInfo> list) {
        if(list == null || list.size() == 0) {
            return;
        }
        BottomRecyclerViewAdapter bottomRecyclerViewAdapter = new BottomRecyclerViewAdapter(this, list);
        bottomAppContent.setAdapter(bottomRecyclerViewAdapter);
        bottomAppContent.setLayoutManager(new GridLayoutManager(this, list.size()));
    }

    @Override
    public void notifyItemLabel(String pkg) {
        if (appsContentAdapter != null) {
            ToastUtils.showToast(this, "安装应用出错:"+pkg);
            appsContentAdapter.notifyLabel(pkg);
        }
    }


    @Subscribe
    public void onItemClick(OnAppItemClickEvent event) {
        startApp = true;
    }

    @Subscribe
    public void onItemRemoveClick(OnAppItemRemoveClickEvent event) {
        ApplicationHelper.performUninstallApp(this, event.getInfo());
    }

    @Subscribe
    public void onPackageChanged(PackageChangedEvent event) {
        if (event.isNewAdd()) {
            AppInfo info = AppInfoUtils.getAppInfoByPkgName(getPackageManager(), event.getPkgName());

            Where<LauncherItemModel> where = new Select()
                    .from(LauncherItemModel.class)
                    .where(LauncherItemModel_Table.apkPkg.eq(event.getPkgName()));
            LauncherItemModel q = where.querySingle();
            LauncherItemModel model = new LauncherItemModel();
            model.position = q == null ? appDataList.size() : q.position;
            model.apkPkg = info.getPkgName();
            model.appLabel = info.getAppLabel();
            model.save();

            appDataList.add(model.position, info);
            Iterator<AppInfo> iterator = appDataList.iterator();
            while (iterator.hasNext()) {
                AppInfo next = iterator.next();
                if (next.getPkgName().equals(event.getPkgName()) && next.isVirtuallApp()) {
                    iterator.remove();
                }
            }
        }
        if (!event.isNewAdd()) {
            AppInfo info = null;
            for (int i = 0; i < appDataList.size(); i++) {
                String pkgName = appDataList.get(i).getPkgName();
                if (pkgName != null && event.getPkgName().equalsIgnoreCase(pkgName)) {
                    info = appDataList.get(i);
                    break;
                }
            }
            SQLite.delete().from(LauncherItemModel.class).where(LauncherItemModel_Table.apkPkg.eq(event.getPkgName())).execute();
            appDataList.remove(info);
        }
        int i = calculateIndicators(appDataList);
        appsContentAdapter.setDataList(appDataList);
    }

    @Override
    public void onPageChange(int position, int itemCount, int pageSize, int page) {
        currentPage = page;
        if (currentPage == 0) {
            timeLayout.setVisibility(View.VISIBLE);
        } else {
            timeLayout.setVisibility(View.GONE);
        }
        pageIndicator.setSelectedPage(currentPage);
    }

    @Override
    public void onTouchActionUp(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP && appsContentAdapter != null) {
            appsContentAdapter.resetState();
        }
    }
}
