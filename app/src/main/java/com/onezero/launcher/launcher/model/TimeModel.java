package com.onezero.launcher.launcher.model;

import com.onezero.launcher.launcher.utils.DateUtil;

/**
 * Created by lizeiwei on 2018/7/28.
 */

public class TimeModel implements ITimeModel {

    @Override
    public String getWeekText() {
        return DateUtil.getWeek();
    }

    @Override
    public String getDateText() {
        return DateUtil.getSysDate();
    }
}
