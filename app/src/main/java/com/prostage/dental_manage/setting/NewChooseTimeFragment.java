package com.prostage.dental_manage.setting;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.prostage.dental_manage.utils.AppConstants.KEY_DATA;
import static com.prostage.dental_manage.utils.AppConstants.KEY_DATA_THREE;
import static com.prostage.dental_manage.utils.AppConstants.KEY_DATA_TWO;

/**
 * Created by congnguyen on 9/1/17.
 */

public class NewChooseTimeFragment extends BaseFragment implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener {
    NewChooseTimeFragmentListener listener;
    private TextView tvTitle;
    private TextView tvBeginTime;
    private TextView tvStopTime;
    private TextView tvCancel;
    private TextView tvOk;
    private CheckBox checkBox;
    public static final String TAG_FROM = "TAG_FROM";
    public static final String TAG_TO = "TAG_TO";
    String title = "";
    String from = "";
    String to = "";
    int position;
    boolean isAM;
    boolean isChoosingFrom;
    boolean isChoosingTo;

    int iHour;
    int iMinute;
    Calendar now;
    TimePickerDialog tpd;

    public static NewChooseTimeFragment newInstance(String title, String from, String to, boolean isAM) {
        Bundle args = new Bundle();
        args.putString(KEY_DATA, title);
        args.putString(KEY_DATA_TWO, from);
        args.putString(KEY_DATA_THREE, to);
        args.putBoolean("isAM", isAM);
        NewChooseTimeFragment fragment = new NewChooseTimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_choose_time, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = (TextView) view.findViewById(R.id.title_txt);
        tvBeginTime = (TextView) view.findViewById(R.id.tv_begin_time);
        tvStopTime = (TextView) view.findViewById(R.id.tv_stop_time);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvOk = (TextView) view.findViewById(R.id.tv_ok);
        checkBox = (CheckBox) view.findViewById(R.id.cb_holiday);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title = getArguments().getString(KEY_DATA);
        from = getArguments().getString(KEY_DATA_TWO);
        to = getArguments().getString(KEY_DATA_THREE);
        isAM = getArguments().getBoolean("isAM");

        tvTitle.setText(title);
        tvBeginTime.setOnClickListener(this);
        tvStopTime.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        checkBox.setOnClickListener(this);

        if (from.equals("0:00") && to.equals("0:00")) {
            tvBeginTime.setText("");
            tvStopTime.setText("");
            checkBox.setChecked(true);
            tvBeginTime.setEnabled(false);
            tvStopTime.setEnabled(false);
            ((GradientDrawable) tvBeginTime.getBackground()).setColor(ContextCompat.getColor(getContext(),
                    R.color.morningBackgroundColor));
            ((GradientDrawable) tvStopTime.getBackground()).setColor(ContextCompat.getColor(getContext(),
                    R.color.morningBackgroundColor));

        } else {
            tvBeginTime.setText(from);
            tvStopTime.setText(to);
            checkBox.setChecked(false);
            tvBeginTime.setEnabled(true);
            tvStopTime.setEnabled(true);
            ((GradientDrawable) tvBeginTime.getBackground()).setColor(Color.WHITE);
            ((GradientDrawable) tvStopTime.getBackground()).setColor(Color.WHITE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_begin_time:
                isChoosingFrom = true;
                isChoosingTo = false;
                loadTimePicker(isAM);
                break;
            case R.id.tv_stop_time:
                isChoosingFrom = false;
                isChoosingTo = true;
                loadTimePicker(isAM);
                break;
            case R.id.cb_holiday:
                if (!checkBox.isChecked()) {
                    tvBeginTime.setText(from);
                    tvBeginTime.setEnabled(true);
                    tvStopTime.setText(to);
                    tvStopTime.setEnabled(true);

                    ((GradientDrawable) tvBeginTime.getBackground()).setColor(Color.WHITE);
                    ((GradientDrawable) tvStopTime.getBackground()).setColor(Color.WHITE);
                } else {
                    tvBeginTime.setText("");
                    tvBeginTime.setEnabled(false);
                    tvStopTime.setText("");
                    tvStopTime.setEnabled(false);
                    ((GradientDrawable) tvBeginTime.getBackground()).setColor(ContextCompat.getColor(getContext(),
                            R.color.morningBackgroundColor));
                    ((GradientDrawable) tvStopTime.getBackground()).setColor(ContextCompat.getColor(getContext(),
                            R.color.morningBackgroundColor));
                }
                break;
            case R.id.tv_ok:
                if (checkBox.isChecked()) {
                    if (listener != null) {
                        listener.onTimeChoose(position, 0, 0, 0, 0);
                        getActivity().onBackPressed();
                    }
                } else {
                    if (validateBlank()) {
                        if (validateValue()) {
                            if (listener != null) {
                                String[] fromTime = from.split(":");
                                String[] toTime = to.split(":");
                                listener.onTimeChoose(position,
                                        Integer.parseInt(fromTime[0].trim()),
                                        Integer.parseInt(fromTime[1].trim()),
                                        Integer.parseInt(toTime[0].trim()),
                                        Integer.parseInt(toTime[1].trim()));

                                getActivity().onBackPressed();
                            }
                        }
                    }
                }
                break;
            case R.id.tv_cancel:
                getActivity().onBackPressed();
                break;

        }
    }

    private boolean validateBlank() {
        boolean isValidate = true;
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        if (TextUtils.isEmpty(tvBeginTime.getText())) {
            tvBeginTime.startAnimation(shake);
            tvBeginTime.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(tvStopTime.getText())) {
            tvStopTime.startAnimation(shake);
            tvStopTime.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        return isValidate;
    }

    private boolean validateValue() {
        String strFrom = tvBeginTime.getText().toString();
        String strTo = tvStopTime.getText().toString();
        boolean isValidate = true;
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        Date d1 = null;
        try {
            d1 = sdf.parse(strFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = sdf.parse(strTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d2 == null || d1 == null) {
            return false;
        }
        long elapsed = d2.getTime() - d1.getTime();
        if (elapsed <= 0) {
            isValidate = false;
            tvBeginTime.startAnimation(shake);
            tvBeginTime.setError(getString(R.string.error_from_to));
            tvStopTime.startAnimation(shake);
            tvStopTime.setError(getString(R.string.error_from_to));
        } else {
            tvBeginTime.setError(null);
            tvStopTime.setError(null);
        }
        return isValidate;
    }

    private String updateTime(int hours, int mins) {
        return new StringBuilder().append(hours).append(':')
                .append((mins < 10 ? ("0" + mins) : mins)).append(" ").toString();
    }

    public void setListener(NewChooseTimeFragmentListener listener, int position) {
        this.listener = listener;
        this.position = position;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if (isChoosingFrom && !isChoosingTo) {
            String time = updateTime(hourOfDay, minute);
            tvBeginTime.setText(time);
            from = time;
        } else if (!isChoosingFrom && isChoosingTo) {
            String time = updateTime(hourOfDay, minute);
            tvStopTime.setText(time);
            to = time;
        }
    }

    public interface NewChooseTimeFragmentListener {
        void onTimeChoose(int position, int hourFrom, int minuteFrom, int hourTo, int minuteTo);
    }

    private void loadTimePicker(boolean isAM) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        tpd.setThemeDark(false);
        tpd.enableSeconds(false);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        if (isAM) {
            tpd.setMaxTime(13, 0, 0);
            tpd.setMinTime(9, 0, 0);
        } else {
            tpd.setMaxTime(18, 0, 0);
            tpd.setMinTime(14, 0, 0);
        }
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }
}
