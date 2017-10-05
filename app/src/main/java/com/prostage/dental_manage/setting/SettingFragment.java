package com.prostage.dental_manage.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.core.IHttpResponse;
import com.prostage.dental_manage.core.NetworkManager;
import com.prostage.dental_manage.core.model.AdminModel;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.ExtendedEditText;
import com.prostage.dental_manage.views.TextViewEdit;
import com.prostage.dental_manage.views.TitleBar;
import com.prostage.dental_manage.views.WeeklyCalendar;

import static com.prostage.dental_manage.utils.AppConstants.ID_ADMIN;
import static com.prostage.dental_manage.utils.AppConstants.ID_SAVE_ADMIN;
import static com.prostage.dental_manage.utils.AppConstants.TAG_ADDRESS;
import static com.prostage.dental_manage.utils.AppConstants.TAG_CLINIC_ITEM;
import static com.prostage.dental_manage.utils.AppConstants.TAG_CLOSED_DAY;
import static com.prostage.dental_manage.utils.AppConstants.TAG_CONTENT_FIRST_NOTIFY;
import static com.prostage.dental_manage.utils.AppConstants.TAG_CONTENT_SECOND_NOTIFY;
import static com.prostage.dental_manage.utils.AppConstants.TAG_PRACTICE_REMARK;
import static com.prostage.dental_manage.utils.AppConstants.TAG_REMARK;
import static com.prostage.dental_manage.utils.AppConstants.TAG_TEL;

/**
 * Created by Linh on 3/30/2017.
 */

public class SettingFragment extends BaseFragment implements IHttpResponse,
        WeeklyCalendar.OnWeeklyCalendarListener, NewChooseTimeFragment.NewChooseTimeFragmentListener,
        TextViewEdit.TextViewEditListener, View.OnClickListener {
    private TitleBar titleBar;
    private ExtendedEditText tvTimeOne;
    private ExtendedEditText tvTimeTwo;
    private TextViewEdit tvFirstContentNotify;
    private TextViewEdit tvSecondContentNotify;
    private TextViewEdit tvAddressHospital;
    private TextViewEdit tvTelHospital;
    private TextViewEdit tvClosedDay;
    private TextViewEdit tvClinicItem;
    private TextViewEdit tvPracticeRemark;
    private TextViewEdit tvRemark;
    private WeeklyCalendar weeklyCalendar;
    private Button btSave;
    NetworkManager networkManager;
    private AdminModel adminModel = new AdminModel();
    private int adminId;

    public static SettingFragment newInstance() {

        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getActivity(), this);
        adminId = Utils.getInt(context, Utils.PREF_ADMIN_ID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        tvTimeOne = (ExtendedEditText) view.findViewById(R.id.tv_time_notify_one);
        tvTimeTwo = (ExtendedEditText) view.findViewById(R.id.tv_time_notify_two);
        tvFirstContentNotify = (TextViewEdit) view.findViewById(R.id.tv_notify_day);
        tvSecondContentNotify = (TextViewEdit) view.findViewById(R.id.tv_notify_three_hours);
        tvAddressHospital = (TextViewEdit) view.findViewById(R.id.tv_address_hospital);
        tvTelHospital = (TextViewEdit) view.findViewById(R.id.tv_tel_hospital);
        tvClosedDay = (TextViewEdit) view.findViewById(R.id.tv_closed_day);
        tvClinicItem = (TextViewEdit) view.findViewById(R.id.tv_clinic_item);
        weeklyCalendar = (WeeklyCalendar) view.findViewById(R.id.weekly_calendar);
        tvPracticeRemark = (TextViewEdit) view.findViewById(R.id.tv_practice_remark);
        tvRemark = (TextViewEdit) view.findViewById(R.id.tv_remark);
        btSave = (Button) view.findViewById(R.id.btSave);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.setting));
        tvFirstContentNotify.setTitle(getString(R.string.first_time));
        tvSecondContentNotify.setTitle(getString(R.string.next_time));
        tvAddressHospital.setTitle(getString(R.string.name_hospital));
        tvTelHospital.setTitle(getString(R.string.tel_hospital));
        tvClosedDay.setTitle(getString(R.string.closed_day));
        tvClinicItem.setTitle(getString(R.string.clinic_item));
        tvPracticeRemark.setTitle(getString(R.string.practice_remark));
        tvRemark.setTitle(getString(R.string.remark));

        tvFirstContentNotify.setListener(TAG_CONTENT_FIRST_NOTIFY, this);
        tvSecondContentNotify.setListener(TAG_CONTENT_SECOND_NOTIFY, this);
        tvAddressHospital.setListener(TAG_ADDRESS, this);
        tvTelHospital.setListener(TAG_TEL, this);
        tvClosedDay.setListener(TAG_CLOSED_DAY, this);
        tvClinicItem.setListener(TAG_CLINIC_ITEM, this);
        tvPracticeRemark.setListener(TAG_PRACTICE_REMARK, this);
        tvRemark.setListener(TAG_REMARK, this);

        if (savedInstanceState == null) {
            networkManager.requestApi(networkManager.getAdminInfoById(adminId), AppConstants.ID_ADMIN);
        } else {
            adminModel = savedInstanceState.getParcelable("adminModel");
            loadInfo();
        }

        weeklyCalendar.setListener(this);
        tvTimeOne.addTextChangedListener(new GenericTextWatcher(tvTimeOne));
        tvTimeTwo.addTextChangedListener(new GenericTextWatcher(tvTimeTwo));
        btSave.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("adminModel", adminModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        Gson gson = new Gson();
        switch (idRequest) {
            case ID_ADMIN:
                adminModel = gson.fromJson(response, AdminModel.class);
                loadInfo();
                break;
            case ID_SAVE_ADMIN:
                Toast.makeText(getActivity(), getString(R.string.save_successful), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {
        switch (idRequest) {
            case ID_ADMIN:
                Toast.makeText(getActivity(), getString(R.string.unknown_error_network), Toast.LENGTH_SHORT).show();
                break;
            case ID_SAVE_ADMIN:
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void loadInfo() {
        tvTimeOne.setText(String.valueOf(adminModel.getNotificationTime1()));
        tvTimeTwo.setText(String.valueOf(adminModel.getNotificationTime2()));

        tvFirstContentNotify.setContent(adminModel.getNotificationText1());
        tvSecondContentNotify.setContent(adminModel.getNotificationText2());

        tvAddressHospital.setContent(adminModel.getAddress());
        tvTelHospital.setContent(adminModel.getTel());

        tvClosedDay.setContent(adminModel.getCloseDays());
        tvClinicItem.setContent(adminModel.getTechnique());
        weeklyCalendar.renderData(adminModel.getWorkingSet());
    }

    @Override
    public void onDayClicked(String title, String begin, String stop, int position, boolean isAm) {
        //ChooseTimeFragment fragment = ChooseTimeFragment.newInstance(title, begin, stop, isAm);
        //fragment.setListener(this, position);
        //add(fragment, "", true, false);
        NewChooseTimeFragment fragment = NewChooseTimeFragment.newInstance(title, begin, stop, isAm);
        fragment.setListener(this, position);
        add(fragment, "", true, false);
    }

    @Override
    public void onTimeChoose(int position, int hourFrom, int minuteFrom, int hourTo, int minuteTo) {
        weeklyCalendar.setWorking(position, hourFrom, minuteFrom, hourTo, minuteTo);
        int truePosition = position / 2;
        if (position % 2 == 0) {
            adminModel.getWorkingSet().get(truePosition).setFirstShiftFromHour(hourFrom);
            adminModel.getWorkingSet().get(truePosition).setFirstShiftFromMin(minuteFrom);
            adminModel.getWorkingSet().get(truePosition).setFirstShiftToHour(hourTo);
            adminModel.getWorkingSet().get(truePosition).setFirstShiftToMin(minuteTo);
        } else {
            adminModel.getWorkingSet().get(truePosition).setSecondShiftFromHour(hourFrom);
            adminModel.getWorkingSet().get(truePosition).setSecondShiftFromMin(minuteFrom);
            adminModel.getWorkingSet().get(truePosition).setSecondShiftToHour(hourTo);
            adminModel.getWorkingSet().get(truePosition).setSecondShiftToMin(minuteTo);
        }

    }

    @Override
    public void onTextViewEditChanged(int tag, String text) {
        switch (tag) {
            case TAG_CONTENT_FIRST_NOTIFY:
                adminModel.setNotificationText1(text);
                break;
            case TAG_CONTENT_SECOND_NOTIFY:
                adminModel.setNotificationText2(text);
                break;
            case TAG_ADDRESS:
                adminModel.setAddress(text);
                break;
            case TAG_TEL:
                adminModel.setTel(text);
                break;
            case TAG_CLOSED_DAY:
                adminModel.setCloseDays(text);
                break;
            case TAG_CLINIC_ITEM:
                adminModel.setTechnique(text);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSave) {
            if (validate()) {
                networkManager.requestPostApi(networkManager.saveAdminInfo(adminModel), ID_SAVE_ADMIN);
            }
        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = s.toString();
            switch (view.getId()) {
                case R.id.tv_time_notify_one:
                    if (TextUtils.isEmpty(text)) {
                        adminModel.setNotificationTime1(0);
                    } else {
                        adminModel.setNotificationTime1(Integer.parseInt(text.trim()));
                    }

                    break;
                case R.id.tv_time_notify_two:
                    if (TextUtils.isEmpty(text)) {
                        adminModel.setNotificationTime2(0);
                    } else {
                        adminModel.setNotificationTime2(Integer.parseInt(text.trim()));
                    }

                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean validate() {
        boolean isValidate = true;
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        if (TextUtils.isEmpty(tvTimeOne.getText())) {
            tvTimeOne.startAnimation(shake);
            tvTimeOne.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(tvTimeTwo.getText())) {
            tvTimeTwo.startAnimation(shake);
            tvTimeTwo.setError(getString(R.string.blank_field));
            isValidate = false;
        }

        if (isValidate) {
            if (adminModel.getNotificationTime1() <= adminModel.getNotificationTime2()) {
                tvTimeOne.setError(getString(R.string.error_time));
                tvTimeTwo.setError(getString(R.string.error_time));
                isValidate = false;
            }
        }

        if (isValidate) {
            tvTimeOne.setError(null);
            tvTimeTwo.setError(null);
        }

        return isValidate;

    }
}
