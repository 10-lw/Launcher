package com.onezero.launcher.launcher.pageRecyclerView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

public class DisableScrollGridManager  extends GridLayoutManager{
    public DisableScrollGridManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DisableScrollGridManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public DisableScrollGridManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
