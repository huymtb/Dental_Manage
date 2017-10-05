package com.prostage.dental_manage.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

/**
 * Created by Linh on 3/29/2017.
 */

public class BaseFragment extends Fragment{
    public void startNewActivity(Class<?> cls, Bundle bundle) {
        ((CommonActivity) getActivity()).startNewActivity(cls, bundle);
    }

    public void replace(BaseFragment fragment, String tag, boolean backstack, boolean animation) {
        ((CommonActivity) getActivity()).replace(fragment, tag, backstack, animation);
    }

    public void replace(BaseFragment fragment, String tag, boolean backstack, boolean animation, int idResourceView) {
        ((CommonActivity) getActivity()).replace(fragment, tag, backstack, animation, idResourceView);
    }

    public void add(BaseFragment fragment, String tag, boolean backstack, boolean animation) {
        ((CommonActivity) getActivity()).add(fragment, tag, backstack, animation);
    }

    public void add(BaseFragment fragment, String tag, boolean backstack, boolean animation, int idResourceView) {
        ((CommonActivity) getActivity()).add(fragment, tag, backstack, animation, idResourceView);
    }

    public void addNoneSlideIn(BaseFragment fragment, String tag, boolean backstack, boolean animation, int idResourceView) {
        ((CommonActivity) getActivity()).addNoneSlideIn(fragment, tag, backstack, animation, idResourceView);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
