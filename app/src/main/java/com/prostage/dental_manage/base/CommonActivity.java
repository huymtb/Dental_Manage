package com.prostage.dental_manage.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.utils.Utils;

public class CommonActivity extends AppCompatActivity {
    public void add(BaseFragment fragment, String tag, boolean backstack, boolean animation) {
        try {
            if (!isFinishing() && !isDestroyed()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (animation)
                    transaction.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.add(R.id.flContainer, fragment, tag);
                if (backstack)
                    transaction.addToBackStack(tag);
                transaction.commitAllowingStateLoss();
            }
        } catch (Exception ignored) {

        }

    }

    public void replace(BaseFragment fragment, String tag, boolean backstack, boolean animation) {
        try {
            if (!isFinishing() && !isDestroyed()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (animation)
                    transaction.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                transaction.replace(R.id.flContainer, fragment, tag);
                if (backstack)
                    transaction.addToBackStack(tag);
                transaction.commitAllowingStateLoss();
            }
        } catch (Exception ignored) {

        }
    }

    public void add(BaseFragment fragment, String tag, boolean backstack, boolean animation, int containViewId) {
        try {
            if (!isFinishing() && !isDestroyed()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (animation)
                    transaction.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.add(containViewId, fragment, tag);
                if (backstack)
                    transaction.addToBackStack(tag);
                transaction.commitAllowingStateLoss();
            }
        } catch (Exception ignored) {

        }
    }

    public void addNoneSlideIn(BaseFragment fragment, String tag, boolean backstack, boolean animation, int containViewId) {
        try {
            if (!isFinishing() && !isDestroyed()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (animation)
                    transaction.setCustomAnimations(0,
                            R.anim.slide_out_left, 0, R.anim.slide_out_right);
                transaction.add(containViewId, fragment, tag);
                if (backstack)
                    transaction.addToBackStack(tag);
                transaction.commitAllowingStateLoss();
            }
        } catch (Exception ignored) {

        }
    }

    public void replace(BaseFragment fragment, String tag, boolean backstack, boolean animation, int containViewId) {
        try {
            if (!isFinishing() && !isDestroyed()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (animation)
                    transaction.setCustomAnimations(R.anim.slide_in_right,
                            R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                transaction.replace(containViewId, fragment, tag);
                if (backstack)
                    transaction.addToBackStack(tag);
                transaction.commitAllowingStateLoss();
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackPressed() {
        Utils.hideSoftKeyboard(this);
        super.onBackPressed();
    }

    public void startNewActivity(Class<?> cls, Bundle bundle) {
        Utils.hideSoftKeyboard(this);
        Intent i = new Intent(this, cls);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        this.startActivity(i);
    }
}
