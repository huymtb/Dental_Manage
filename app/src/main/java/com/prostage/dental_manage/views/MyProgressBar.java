package com.prostage.dental_manage.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.prostage.dental_manage.R;

/**
 * Created by congnc on 5/15/17.
 */

public class MyProgressBar extends RelativeLayout {
    View rootView;

    public MyProgressBar(Context context) {
        super(context);
        init(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        rootView = inflate(context, R.layout.view_progress_bar, this);
        RefreshView progressBar = (RefreshView) rootView.findViewById(R.id.progressbar_loading);
        progressBar.pullProgress(0, 0.8f);
        progressBar.refreshing();
    }
}
