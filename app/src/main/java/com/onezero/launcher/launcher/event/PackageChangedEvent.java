package com.onezero.launcher.launcher.event;

public class PackageChangedEvent {
    private String pkgName;
    private boolean newAdd;// true means new add, false means remove

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public boolean isNewAdd() {
        return newAdd;
    }

    public void setNewAdd(boolean newAdd) {
        this.newAdd = newAdd;
    }

    public PackageChangedEvent(String pkgName, boolean newAdd) {
        this.pkgName = pkgName;
        this.newAdd = newAdd;
    }
}
