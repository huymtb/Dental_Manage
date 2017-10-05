package com.prostage.dental_manage.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.prostage.dental_manage.calendar.ReservationFragment;
import com.prostage.dental_manage.register.RegisterFragment;
import com.prostage.dental_manage.chat.ChatFragment;
import com.prostage.dental_manage.setting.SettingFragment;
import com.prostage.dental_manage.user.SearchUserFragment;

class FeaturesPagerAdapter extends FragmentPagerAdapter {
    private ReservationFragment reservationFragment;
    private ChatFragment chatFragment;
    private SearchUserFragment userFragment;
    private RegisterFragment registerFragment;
    private SettingFragment settingFragment;

    FeaturesPagerAdapter(FragmentManager fm) {
        super(fm);
        reservationFragment = ReservationFragment.newInstance();
        chatFragment = ChatFragment.newInstance();
        userFragment = SearchUserFragment.newInstance();
        registerFragment = RegisterFragment.newInstance(-1, "");
        settingFragment = SettingFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return reservationFragment;
            case 1:
                return chatFragment;
            case 2:
                return userFragment;
            case 3:
                return registerFragment;
            case 4:
                return settingFragment;
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= getCount()) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
