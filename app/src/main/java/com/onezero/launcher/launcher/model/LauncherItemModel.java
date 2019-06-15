package com.onezero.launcher.launcher.model;

import com.onezero.launcher.launcher.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class LauncherItemModel extends BaseModel {

    @Column
    public String appLabel;

    @Column
    @PrimaryKey
    public String apkPkg;

    @Column
    public int position;
}
