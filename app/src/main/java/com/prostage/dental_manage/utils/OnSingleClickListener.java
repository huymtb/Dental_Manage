package com.prostage.dental_manage.utils;

import android.util.Log;
import android.view.View;

/**
 * Created by Linh on 3/29/2017.
 */

public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long MIN_DELAY_MS = 1000L;

    private long mLastClickTime;

    @Override
    public final void onClick(View v) {
        long lastClickTime = mLastClickTime;
        long now = System.currentTimeMillis();
        mLastClickTime = now;
        if (now - lastClickTime < MIN_DELAY_MS) {
            // Too fast: ignore
            Log.v("OnSingleClickListener", "ignore");
        } else {
            // Register the click
            onSingleClick(v);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    public abstract void onSingleClick(View v);
}
