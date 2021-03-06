package com.onezero.launcher.launcher.pageRecyclerView;

import android.content.Context;

import com.onezero.launcher.launcher.view.PageTurningSimpleDirection;

public class PageTurningSimpleDetector {
    public static int detectBothAxisTuring(Context context, int x, int y) {
        int x1 = Math.abs(x);
        int y1 = Math.abs(y);
        if (x1 > 50) {
            if (x < 0 ) {
                return PageTurningSimpleDirection.NEXT;
            } else {
                return PageTurningSimpleDirection.PREV;
            }
        }
        return PageTurningSimpleDirection.NONE;
    }
}
