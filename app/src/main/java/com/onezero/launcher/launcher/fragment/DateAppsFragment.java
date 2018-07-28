package com.onezero.launcher.launcher.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.onezero.launcher.launcher.DateReceiver;
import com.onezero.launcher.launcher.R;
import com.onezero.launcher.launcher.presenter.LauncherPresenter;
import com.onezero.launcher.launcher.utils.StringUtils;
import com.onezero.launcher.launcher.view.ITimeView;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class DateAppsFragment extends BaseFragment implements ITimeView {
    private TextView weekText;
    private TextView dateText;
    private LauncherPresenter presenter;
    private DateReceiver dateReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_launcher_date_apps;
    }

    @Override
    protected void initViews(View view) {
        weekText = view.findViewById(R.id.launcher_week_text);
        dateText = view.findViewById(R.id.launcher_date_text);
    }

    @Override
    protected void initData() {
        presenter = new LauncherPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateTime();
        initReceiver();
    }

    private void initReceiver() {
        dateReceiver = new DateReceiver(presenter);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
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
}
