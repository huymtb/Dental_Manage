package com.prostage.dental_manage.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.prostage.dental_manage.utils.AppConstants;

import java.util.ArrayList;

class RequestPageAdapter extends FragmentPagerAdapter {
    static final int REQUEST_TODAY = 0;
    static final int REQUEST_TOMORROW = 1;
    static final int REQUEST_NEXT_DAY = 2;

    private ArrayList<ScheduleViewFragment> viewFragment;

    RequestPageAdapter(FragmentManager fm) {
        super(fm);
        viewFragment = new ArrayList<>();
        viewFragment.add(ScheduleViewFragment.newInstance(AppConstants.ID_FRAGMENT_TODAY));
        viewFragment.add(ScheduleViewFragment.newInstance(AppConstants.ID_FRAGMENT_TOMORROW));
        viewFragment.add(ScheduleViewFragment.newInstance(AppConstants.ID_FRAGMENT_NEXT_DAY));
    }

    @Override
    public Fragment getItem(int position) {
        return viewFragment.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= getCount()) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    void refreshReservations() {
        for (int i = 0; i < viewFragment.size(); i++) {
            viewFragment.get(i).getListUser();
        }
    }
}
