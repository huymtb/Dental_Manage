package com.prostage.dental_manage.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

/**
 * Created by congnguyen on 8/31/17.
 */

public class RangeTimePicker extends TimePicker implements TimePicker.OnTimeChangedListener {
    private int mMinHour = -1;

    private int mMinMinute = -1;

    private int mMaxHour = 25;

    private int mMaxMinute = 61;

    private int mCurrentHour;

    private int mCurrentMinute;
    public RangeTimePicker(Context context) {
        super(context);
        setOnTimeChangedListener(this);
    }

    public RangeTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTimeChangedListener(this);
    }

    public RangeTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTimeChangedListener(this);
    }

    public void setMaxTime(int hourIn24, int minute) {
        mMaxHour = hourIn24;
        mMaxMinute = minute;

        LocalTime currentTime = LocalTime.now();
        LocalTime maxTime = new LocalTime(mMaxHour, mMaxMinute);

        if (currentTime.isAfter(maxTime)) {
            //this.setCurrentHour(mCurrentHour = currentTime.getHourOfDay());
            this.setHour(mCurrentHour = currentTime.getHourOfDay());
//            this.setCurrentMinute(
//                    mCurrentMinute = currentTime.getMinuteOfHour());
            this.setMinute(mCurrentMinute = currentTime.getMinuteOfHour());
        }

    }

    public void setMinTime(int hourIn24, int minute) {
        mMinHour = hourIn24;
        mMinMinute = minute;

        LocalTime currentTime = LocalTime.now();
        LocalTime minTime = new LocalTime(mMinHour, mMinMinute);

        if (currentTime.isBefore(minTime)) {
            //this.setCurrentHour(mCurrentHour = minTime.getHourOfDay());
            this.setHour(mCurrentHour = currentTime.getHourOfDay());
            //this.setCurrentMinute(mCurrentMinute = minTime.getMinuteOfHour());
            this.setMinute(mCurrentMinute = minTime.getMinuteOfHour());
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        boolean validTime = true;
        if (hourOfDay < mMinHour || (hourOfDay == mMinHour
                && minute < mMinMinute)) {
            validTime = false;
        }

        if (hourOfDay > mMaxHour || (hourOfDay == mMaxHour
                && minute > mMaxMinute)) {
            validTime = false;
        }

        if (validTime) {
            mCurrentHour = hourOfDay;
            mCurrentMinute = minute;
        }

        //setCurrentHour(mCurrentHour);
        setHour(mCurrentHour);
        //setCurrentMinute(mCurrentMinute);
        setMinute(mCurrentMinute);
    }
}
