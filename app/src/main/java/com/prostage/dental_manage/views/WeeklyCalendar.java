package com.prostage.dental_manage.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.core.model.WorkingDayModel;
import com.prostage.dental_manage.utils.OnSingleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnc on 4/3/17.
 */

public class WeeklyCalendar extends TableLayout {
//    private static final int MORNING = 1;
//    private static final int AFTERNOON = 2;
    private View rootView;
    private TextView tvMonAM, tvMonPM;
    private TextView tvTueAM, tvTuePM;
    private TextView tvWebAM, tvWebPM;
    private TextView tvThuAM, tvThuPM;
    private TextView tvFriAM, tvFriPM;
    private TextView tvSatAM, tvSatPM;
    private TextView tvSunAM, tvSunPM;
    private TextView tvHolAM, tvHolPM;
    private ArrayList<TextView> listTextView = new ArrayList<>(16);
    private ArrayList<WorkingDayModel> listWork = new ArrayList<>(16);

//    private int type;
    private OnWeeklyCalendarListener listener;

    public static final int MONDAY_MORNING = 1;
    public static final int MONDAY_AFTERNOON = 2;
    public static final int TUESDAY_MORNING = 3;
    public static final int TUESDAY_AFTERNOON = 4;


    public WeeklyCalendar(Context context) {
        super(context);
        init(context);
    }

    public WeeklyCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_calendar, this);
        tvMonAM = (TextView) rootView.findViewById(R.id.tv_monday_morning);
        tvMonPM = (TextView) rootView.findViewById(R.id.tv_monday_afternoon);
        tvTueAM = (TextView) rootView.findViewById(R.id.tv_tuesday_morning);
        tvTuePM = (TextView) rootView.findViewById(R.id.tv_tuesday_afternoon);
        tvWebAM = (TextView) rootView.findViewById(R.id.tv_wednesday_morning);
        tvWebPM = (TextView) rootView.findViewById(R.id.tv_wednesday_afternoon);
        tvThuAM = (TextView) rootView.findViewById(R.id.tv_thursday_morning);
        tvThuPM = (TextView) rootView.findViewById(R.id.tv_thursday_afternoon);
        tvFriAM = (TextView) rootView.findViewById(R.id.tv_friday_morning);
        tvFriPM = (TextView) rootView.findViewById(R.id.tv_friday_afternoon);
        tvSatAM = (TextView) rootView.findViewById(R.id.tv_saturday_morning);
        tvSatPM = (TextView) rootView.findViewById(R.id.tv_saturday_afternoon);
        tvSunAM = (TextView) rootView.findViewById(R.id.tv_sunday_morning);
        tvSunPM = (TextView) rootView.findViewById(R.id.tv_sunday_afternoon);
        tvHolAM = (TextView) rootView.findViewById(R.id.tv_holiday_morning);
        tvHolPM = (TextView) rootView.findViewById(R.id.tv_holiday_afternoon);

        tvMonAM.setOnClickListener(mySingleClickListener);
        tvMonPM.setOnClickListener(mySingleClickListener);
        tvTueAM.setOnClickListener(mySingleClickListener);
        tvTuePM.setOnClickListener(mySingleClickListener);
        tvWebAM.setOnClickListener(mySingleClickListener);
        tvWebPM.setOnClickListener(mySingleClickListener);
        tvThuAM.setOnClickListener(mySingleClickListener);
        tvThuPM.setOnClickListener(mySingleClickListener);
        tvFriAM.setOnClickListener(mySingleClickListener);
        tvFriPM.setOnClickListener(mySingleClickListener);
        tvSatAM.setOnClickListener(mySingleClickListener);
        tvSatPM.setOnClickListener(mySingleClickListener);
        tvSunAM.setOnClickListener(mySingleClickListener);
        tvSunPM.setOnClickListener(mySingleClickListener);
        tvHolAM.setOnClickListener(mySingleClickListener);
        tvHolPM.setOnClickListener(mySingleClickListener);

        listTextView.add(tvMonAM);
        listTextView.add(tvMonPM);
        listTextView.add(tvTueAM);
        listTextView.add(tvTuePM);
        listTextView.add(tvWebAM);
        listTextView.add(tvWebPM);
        listTextView.add(tvThuAM);
        listTextView.add(tvThuPM);
        listTextView.add(tvFriAM);
        listTextView.add(tvFriPM);
        listTextView.add(tvSatAM);
        listTextView.add(tvSatPM);
        listTextView.add(tvSunAM);
        listTextView.add(tvSunPM);
        listTextView.add(tvHolAM);
        listTextView.add(tvHolPM);
    }

    public void renderData(List<WorkingDayModel> list) {
        for (int i = 0; i < list.size(); i++) {
            String textFrom = getFormatWorking(list.get(i).getFirstShiftFromHour(),
                    list.get(i).getFirstShiftFromMin(),
                    list.get(i).getFirstShiftToHour(),
                    list.get(i).getFirstShiftToMin());
            String textTo = getFormatWorking(list.get(i).getSecondShiftFromHour(),
                    list.get(i).getSecondShiftFromMin(),
                    list.get(i).getSecondShiftToHour(),
                    list.get(i).getSecondShiftToMin());
            listTextView.get(i * 2).setText(textFrom);
            listTextView.get(i * 2 + 1).setText(textTo);
        }

        listWork.clear();
        listWork.addAll(list);
    }

    public String getFormatWorking(int beginHour, int beginMinute, int stopHour, int stopMinute) {
        if (beginHour == 0 && beginMinute == 0 && stopHour == 0 && stopMinute == 0) {
            return "X";
        } else {
            return beginHour + ":" + (beginMinute < 10 ? ("0" + beginMinute) : beginMinute)
                    + " ~ " + stopHour + ":" + (stopMinute < 10 ? ("0" + stopMinute) : stopMinute);
        }
    }

    public void setWorking(int position, int beginHour, int beginMinute, int stopHour, int stopMinute) {
        WorkingDayModel model = new WorkingDayModel();
        model.setFirstShiftFromHour(beginHour);

        listTextView.get(position).setText(getFormatWorking(beginHour, beginMinute, stopHour, stopMinute));
    }

    public interface OnWeeklyCalendarListener {
        void onDayClicked(String title, String begin, String stop, int position, boolean isAM);
    }

    public void setListener(OnWeeklyCalendarListener listener) {
        this.listener = listener;
    }

    private String getBeginTime(String time) {
        if (time.equals("X")) {
            return "0:00";
        }
        String[] parts = time.trim().split("~");
        return parts[0].trim();
    }

    private String getStopTime(String time) {
        if (time.equals("X")) {
            return "0:00";
        }
        String[] parts = time.trim().split("~");
        return parts[1].trim();
    }

    private OnSingleClickListener mySingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            String time = "";
            switch (v.getId()) {
                case R.id.tv_monday_morning:
                    time = tvMonAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.monday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 0, true);
                    }
                    break;
                case R.id.tv_monday_afternoon:
                    time = tvMonPM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.monday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 1, false);
                    }
                    break;
                case R.id.tv_tuesday_morning:
                    time = tvTueAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.tuesday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 2, true);
                    }
                    break;
                case R.id.tv_tuesday_afternoon:
                    time = tvTuePM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.tuesday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 3, false);
                    }
                    break;
                case R.id.tv_wednesday_morning:
                    time = tvWebAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.wednesday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 4, true);
                    }
                    break;
                case R.id.tv_wednesday_afternoon:
                    time = tvWebPM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.wednesday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 5, false);
                    }
                    break;
                case R.id.tv_thursday_morning:
                    time = tvThuAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.thursday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 6, true);
                    }
                    break;
                case R.id.tv_thursday_afternoon:
                    time = tvThuPM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.thursday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 7, false);
                    }
                    break;
                case R.id.tv_friday_morning:
                    time = tvFriAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.friday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 8, true);
                    }
                    break;
                case R.id.tv_friday_afternoon:
                    time = tvFriPM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.friday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 9, false);
                    }
                    break;
                case R.id.tv_saturday_morning:
                    time = tvSatAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.saturday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 10, true);
                    }
                    break;
                case R.id.tv_saturday_afternoon:
                    time = tvSatPM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.saturday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 11, false);
                    }
                    break;
                case R.id.tv_sunday_morning:
                    time = tvSunAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.sunday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 12, true);
                    }
                    break;
                case R.id.tv_sunday_afternoon:
                    time = tvSunPM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.sunday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 13, false);
                    }
                    break;
                case R.id.tv_holiday_morning:
                    time = tvHolAM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.holiday) + " - " + getResources().getString(R.string.morning),
                                getBeginTime(time), getStopTime(time), 14, true);
                    }
                    break;
                case R.id.tv_holiday_afternoon:
                    time = tvHolPM.getText().toString();
                    if (listener != null) {
                        listener.onDayClicked(
                                getResources().getString(R.string.holiday) + " - " + getResources().getString(R.string.afternoon),
                                getBeginTime(time), getStopTime(time), 15, false);
                    }
                    break;
            }
        }
    };
}
