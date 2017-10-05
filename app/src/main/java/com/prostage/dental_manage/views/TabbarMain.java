package com.prostage.dental_manage.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.utils.Utils;

/**
 * Created by Linh on 3/29/2017.
 */

public class TabbarMain extends RelativeLayout implements View.OnClickListener {
    private View rootView;

    private RelativeLayout calendarLayout;
    private ImageView ivCalendar;
    private TextView tvCalendar;

    private RelativeLayout chatLayout;
    private ImageView ivChat;
    private TextView tvChat;
    private TextView tvNumberChat;
    //private ImageView ivUnreadChat;

    private RelativeLayout userLayout;
    private ImageView ivUser;
    private TextView tvUser;

    private RelativeLayout addLayout;
    private ImageView ivAdd;
    private TextView tvAdd;

    private RelativeLayout settingLayout;
    private ImageView ivSetting;
    private TextView tvSetting;

    private int colorEnable;
    private int colorDisable;

    private OnTabbarMainListener listener;

    public static final int CALENDAR_INDEX = 1;
    public static final int CHAT_INDEX = 2;
    public static final int USER_INDEX = 3;
    public static final int ADD_INDEX = 4;
    public static final int SETTING_INDEX = 5;

    private int currentTab = CALENDAR_INDEX;

    public TabbarMain(Context context) {
        super(context);
        init(context);
    }

    public TabbarMain(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabbarMain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_tabbar_main, this);

        calendarLayout = (RelativeLayout) rootView.findViewById(R.id.calendar_layout);
        ivCalendar = (ImageView) rootView.findViewById(R.id.iv_calendar);
        tvCalendar = (TextView) rootView.findViewById(R.id.tv_calendar);
        calendarLayout.setOnClickListener(this);

        chatLayout = (RelativeLayout) rootView.findViewById(R.id.chat_layout);
        ivChat = (ImageView) rootView.findViewById(R.id.iv_chat);
        tvChat = (TextView) rootView.findViewById(R.id.tv_chat);
        tvNumberChat = (TextView) rootView.findViewById(R.id.tv_badge_number_chat);
        //ivUnreadChat = (ImageView) rootView.findViewById(R.id.iv_unread_chat);
        chatLayout.setOnClickListener(this);

        userLayout = (RelativeLayout) rootView.findViewById(R.id.user_layout);
        ivUser = (ImageView) rootView.findViewById(R.id.iv_user);
        tvUser = (TextView) rootView.findViewById(R.id.tv_user);
        userLayout.setOnClickListener(this);

        addLayout = (RelativeLayout) rootView.findViewById(R.id.add_layout);
        ivAdd = (ImageView) rootView.findViewById(R.id.iv_add);
        tvAdd = (TextView) rootView.findViewById(R.id.tv_add);
        addLayout.setOnClickListener(this);

        settingLayout = (RelativeLayout) rootView.findViewById(R.id.setting_layout);
        ivSetting = (ImageView) rootView.findViewById(R.id.iv_setting);
        tvSetting = (TextView) rootView.findViewById(R.id.tv_setting);
        settingLayout.setOnClickListener(this);

        colorEnable = (Utils.getColor(getContext(), R.color.white));
        colorDisable = (Utils.getColor(getContext(), R.color.colorDisable));

        changeTab(currentTab);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calendar_layout:
                changeTab(CALENDAR_INDEX);
                break;
            case R.id.chat_layout:
                changeTab(CHAT_INDEX);
                break;
            case R.id.user_layout:
                changeTab(USER_INDEX);
                break;
            case R.id.add_layout:
                changeTab(ADD_INDEX);
                break;
            case R.id.setting_layout:
                changeTab(SETTING_INDEX);
                break;
        }
    }

    public void setListener(OnTabbarMainListener listener) {
        this.listener = listener;
    }

    public interface OnTabbarMainListener {
        void onChangeTabMain(int index);
    }

    public void changeTab(int index) {
        if (listener != null) {
            listener.onChangeTabMain(index);
        }
        if (index == CALENDAR_INDEX) {
            ivCalendar.setImageResource(R.mipmap.ic_calendar);
            tvCalendar.setTextColor(colorEnable);
        } else {
            ivCalendar.setImageResource(R.mipmap.ic_calendar_s);
            tvCalendar.setTextColor(colorDisable);
        }

        if (index == CHAT_INDEX) {
            ivChat.setImageResource(R.mipmap.ic_chat);
            tvChat.setTextColor(colorEnable);
        } else {
            ivChat.setImageResource(R.mipmap.ic_chat_s);
            tvChat.setTextColor(colorDisable);
        }

        if (index == USER_INDEX) {
            ivUser.setImageResource(R.mipmap.ic_user);
            tvUser.setTextColor(colorEnable);
        } else {
            ivUser.setImageResource(R.mipmap.ic_user_s);
            tvUser.setTextColor(colorDisable);
        }

        if (index == ADD_INDEX) {
            ivAdd.setImageResource(R.mipmap.ic_add);
            tvAdd.setTextColor(colorEnable);
        } else {
            ivAdd.setImageResource(R.mipmap.ic_add_s);
            tvAdd.setTextColor(colorDisable);
        }

        if (index == SETTING_INDEX) {
            ivSetting.setImageResource(R.mipmap.ic_setting);
            tvSetting.setTextColor(colorEnable);
        } else {
            ivSetting.setImageResource(R.mipmap.ic_setting_s);
            tvSetting.setTextColor(colorDisable);
        }
    }

    public void setNumberChat(int numberChat) {
        if (numberChat > 0) {
            tvNumberChat.setVisibility(VISIBLE);
            tvNumberChat.setText(String.valueOf(numberChat));
            ((GradientDrawable) tvNumberChat.getBackground()).setStroke((int) Utils.dip2px(getContext(), 1.5f),
                    Utils.getColor(getContext(), R.color.colorPrimary));
            ((GradientDrawable) tvNumberChat.getBackground()).setColor(Color.WHITE);
        } else {
            tvNumberChat.setVisibility(INVISIBLE);
        }
    }
}
