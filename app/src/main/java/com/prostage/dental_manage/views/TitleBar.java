package com.prostage.dental_manage.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.utils.OnSingleClickListener;

import static com.prostage.dental_manage.R.id.tv_subtitle_bar;

/**
 * Created by Linh on 3/29/2017.
 */

public class TitleBar extends RelativeLayout {
    private View rootView;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private ImageView ivIconRight;

    private OnTitleBarListener listener;

    private OnSingleClickListener singleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    if (listener != null) {
                        listener.onClickBackTitleBar();
                    }
                    break;
                case R.id.iv_log_out:
                    if (listener != null) {
                        listener.onClickRightIcon();
                    }
            }
        }
    };

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_title_bar, this);
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title_bar);
        tvSubtitle = (TextView) rootView.findViewById(tv_subtitle_bar);
        ivIconRight = (ImageView) rootView.findViewById(R.id.iv_log_out);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setSubtitle(String subtitle) {
        tvSubtitle.setText(subtitle);
        tvSubtitle.setVisibility(VISIBLE);
    }

    public void setIconBack(int icon) {
        ivBack.setImageResource(icon);
        ivBack.setVisibility(VISIBLE);
        ivBack.setOnClickListener(singleClickListener);
    }

    public void addIconRight() {
        ivIconRight.setVisibility(VISIBLE);
        ivIconRight.setOnClickListener(singleClickListener);
    }

    public interface OnTitleBarListener {
        void onClickBackTitleBar();

        void onClickRightIcon();
    }

    public void setListener(OnTitleBarListener listener) {
        this.listener = listener;
    }
}
