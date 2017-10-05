package com.prostage.dental_manage.register;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.base.EventDistributor;
import com.prostage.dental_manage.core.IHttpResponse;
import com.prostage.dental_manage.core.NetworkManager;
import com.prostage.dental_manage.core.WanaKanaJava;
import com.prostage.dental_manage.core.kanji.KanjiNetworkManager;
import com.prostage.dental_manage.core.model.RegisterModel;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.ExtendedEditText;
import com.prostage.dental_manage.views.TitleBar;

import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.prostage.dental_manage.utils.AppConstants.ID_FIRST_NICKNAME;
import static com.prostage.dental_manage.utils.AppConstants.ID_LAST_NICKNAME;
import static com.prostage.dental_manage.utils.AppConstants.KEY_DATA;
import static com.prostage.dental_manage.utils.AppConstants.KEY_DATA_TWO;

public class RegisterFragment extends BaseFragment implements TitleBar.OnTitleBarListener,
        View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        IHttpResponse, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "RegisterFragment";
    //public static final String TAG_EDIT = TAG + "EDIT";
    private TitleBar titleBar;
    private RegisterModel registerModel = new RegisterModel();
    Animation shake;

    private ExtendedEditText tvUserCode, tvFirstName, tvLastName, tvFirstNickName, tvLastNickName, tvNote;

    private CheckBox cbNotify;
    private RadioGroup radioGroup;
    private RadioButton rdMale, rdFemale;
    private TextView tvNotify, tvBirthday, tvAppointmentDay;
    private Button btSave;
    NetworkManager networkManager;
    KanjiNetworkManager kanjiNetworkManager;
    WanaKanaJava wanaKana;
    String isHiraganaRegEx;
    String isJapaneseRegEx;
    InputFilter[] filters;
    int gender;

    int iMonth;
    int iDay;
    int iYear;

    int iHour;
    int iMinute;
    Calendar now;
    int id = -1;
    String userCode;

    DatePickerDialog dpd;
    TimePickerDialog tpd;
    private int clickWhat;
    private final int CLICK_BIRTHDAY = 0;
    private final int CLICK_APPOINTMENT = 1;

    public static RegisterFragment newInstance(int idUser, String userCode) {
        Bundle args = new Bundle();
        args.putInt(KEY_DATA, idUser);
        args.putString(KEY_DATA_TWO, userCode);
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        wanaKana = new WanaKanaJava(true);
        isHiraganaRegEx = "^(.{0}|[ぁ-ゞー]{1,32})$";
        isJapaneseRegEx = "^(.{0}|[ぁ-ゞァ-ヶー々〇?\\x{3400}-\\x{9FFF}\\x{F900}-\\x{FAFF}\\x{20000}-\\x{2FFFF}]{1,32})$";
        networkManager = new NetworkManager(getActivity(), this);
        kanjiNetworkManager = new KanjiNetworkManager(getActivity(), this);
        filters = new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    if (source.equals("")) {
                        return source;
                    }
                    if (source.toString().matches(isJapaneseRegEx)) {
                        return source;
                    }
                    return "";
                }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        cbNotify = (CheckBox) view.findViewById(R.id.cb_notify);
        tvNotify = (TextView) view.findViewById(R.id.tv_notify);
        tvBirthday = (TextView) view.findViewById(R.id.tv_birthday);
        tvAppointmentDay = (TextView) view.findViewById(R.id.tv_appointment);
        btSave = (Button) view.findViewById(R.id.bt_save);
        tvUserCode = (ExtendedEditText) view.findViewById(R.id.tv_user_id);
        tvLastName = (ExtendedEditText) view.findViewById(R.id.tv_last_name);
        tvFirstName = (ExtendedEditText) view.findViewById(R.id.tv_first_name);
        tvLastNickName = (ExtendedEditText) view.findViewById(R.id.tv_last_nickname);
        tvFirstNickName = (ExtendedEditText) view.findViewById(R.id.tv_first_nickname);
        tvNote = (ExtendedEditText) view.findViewById(R.id.tv_note);
        radioGroup = (RadioGroup) view.findViewById(R.id.rg_sex_group);
        rdMale = (RadioButton) view.findViewById(R.id.rb_male);
        rdFemale = (RadioButton) view.findViewById(R.id.rb_female);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (getTag().equals(TAG_EDIT)) {
//            titleBar.setListener(this);
//        }
        titleBar.setTitle(getString(R.string.add));
//        if (getTag().equals(TAG_EDIT)) {
//            setVisibleNotify(true);
//            titleBar.setIconBack(R.mipmap.ic_back);
//            tvUserCode.setEnabled(false);
//
//            if (savedInstanceState == null) {
//                id = getArguments().getInt(KEY_DATA);
//                userCode = getArguments().getString(KEY_DATA_TWO);
//            } else {
//                id = savedInstanceState.getInt("userId");
//                userCode = savedInstanceState.getString("userCode");
//            }
//            ((GradientDrawable) tvUserCode.getBackground()).setColor(ContextCompat.getColor(getContext(),
//                    R.color.morningBackgroundColor));
//
//            networkManager.requestApi(networkManager.getUserInfoById(id), ID_USER_INFO);
//        } else {
        titleBar.setTitle(getString(R.string.add));
        ((GradientDrawable) tvUserCode.getBackground()).setColor(Color.WHITE);
        setVisibleNotify(false);
        rdMale.setChecked(true);
        rdMale.performClick();
        //gender = 1;
//        }

        tvBirthday.setOnClickListener(this);
        tvAppointmentDay.setOnClickListener(this);
        btSave.setOnClickListener(this);

        tvUserCode.addTextChangedListener(new GenericTextWatcher(tvUserCode));
        tvLastName.addTextChangedListener(new GenericTextWatcher(tvLastName));
        tvFirstName.addTextChangedListener(new GenericTextWatcher(tvFirstName));
        tvLastNickName.addTextChangedListener(new GenericTextWatcher(tvLastNickName));
        tvFirstNickName.addTextChangedListener(new GenericTextWatcher(tvFirstNickName));
        tvNote.addTextChangedListener(new GenericTextWatcher(tvNote));
        radioGroup.setOnCheckedChangeListener(this);

        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        tvLastName.setFilters(filters);
        tvFirstName.setFilters(filters);
        tvLastNickName.setFilters(filters);
        tvFirstNickName.setFilters(filters);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("userId", id);
        outState.putString("userCode", userCode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClickBackTitleBar() {
        Utils.hideSoftKeyboard(getActivity());
        getActivity().runOnUiThread(() -> {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }, 300);
        });
    }

    @Override
    public void onClickRightIcon() {

    }

    private void setVisibleNotify(boolean b) {
        if (b) {
            cbNotify.setVisibility(View.VISIBLE);
            tvNotify.setVisibility(View.VISIBLE);
        } else {
            cbNotify.setVisibility(View.GONE);
            tvNotify.setVisibility(View.GONE);
        }
    }

    private void initTime() {
        now = Calendar.getInstance();
        //iMonth = now.get(Calendar.MONTH);
        iMonth = 1;
        iDay = 1;
        //iDay = now.get(Calendar.DAY_OF_MONTH);
        //iYear = now.get(Calendar.YEAR) - AppConstants.MIN_AGE;
        iYear = 1980;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        iDay = dayOfMonth;
        iMonth = month;
        iYear = year;

        if (clickWhat == CLICK_BIRTHDAY) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            Date date = calendar.getTime();
            String birthday;
            birthday = Utils.getStringDateByFormat(getActivity(), date, "yyyy/MM/dd");
            tvBirthday.setText(birthday);
            tvBirthday.setError(null);
            registerModel.setBirthday(birthday);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (clickWhat == CLICK_APPOINTMENT) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(iYear, iMonth, iDay, hourOfDay, minute);
            Date date = calendar.getTime();
            String birthday;
            birthday = Utils.getStringDateByFormat(getActivity(), date, "yyyy/MM/dd HH:mm:ss");
            tvAppointmentDay.setText(birthday);
            tvAppointmentDay.setError(null);
            registerModel.setReservationDate(birthday);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_birthday:
                Utils.hideSoftKeyboard(getActivity());
                clickWhat = CLICK_BIRTHDAY;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dpd = new DatePickerDialog(getActivity(), R.style.style_date_picker_dialog, this, iYear, iMonth, iDay);
                } else {
                    dpd = new DatePickerDialog(getActivity(), this, iYear, iMonth, iDay);
                }
                dpd.getDatePicker().setMaxDate(now.getTimeInMillis());
                dpd.show();
                break;
            case R.id.tv_appointment:
                Utils.hideSoftKeyboard(getActivity());
                clickWhat = CLICK_APPOINTMENT;
                tpd = new TimePickerDialog(getActivity(), this, iHour, iMinute, true);
                tpd.show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dpd = new DatePickerDialog(getActivity(), R.style.style_date_picker_dialog,
                            this, iYear, iMonth, iDay);
                } else {
                    dpd = new DatePickerDialog(getActivity(), this, iYear, iMonth, iDay);
                }
                dpd.getDatePicker().setMinDate(now.getTimeInMillis());
                dpd.show();
                break;
            case R.id.bt_save:
                int adminId = Utils.getInt(getActivity(), Utils.PREF_ADMIN_ID);
                if (validation()) {
                    registerModel.setAdminId(adminId);
                    registerModel.setGender(gender);
                    if (id != -1) {
                        String reservationDate = registerModel.getReservationDate();
                        reservationDate += ":00";
                        registerModel.setReservationDate(reservationDate);
                        networkManager.requestPostApi(networkManager.updateUserInfo(registerModel),
                                AppConstants.ID_UPDATE_USER);
                    } else {
                        networkManager.requestPostApi(networkManager.saveUserInfo(registerModel),
                                AppConstants.ID_SAVE_USER);
                    }

                }
                break;
        }
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        Gson gson = new Gson();
        switch (idRequest) {
            case AppConstants.ID_SAVE_USER:
                Toast.makeText(getActivity(), getString(R.string.add_user_successfully),
                        Toast.LENGTH_SHORT).show();
                resetInfo();
                EventDistributor.getInstance().sendUpdateReservation();
                EventDistributor.getInstance().sendUpdateNewChat();
                break;
            case AppConstants.ID_UPDATE_USER:
                Toast.makeText(getActivity(), getString(R.string.update_user_successfully),
                        Toast.LENGTH_SHORT).show();
                EventDistributor.getInstance().sendUpdateReservation();
                onClickBackTitleBar();
                break;
            case AppConstants.ID_USER_INFO:
                UserModel model = gson.fromJson(response, UserModel.class);
                if (model != null) {
                    String date = model.getReservations().get(0).getReservationDate();
                    String formattedDate = Utils.formatDateFromTo(date,
                            "yyyy-MM-dd HH:mm:ss.S", "yyyy/MM/dd HH:mm");

                    String birth = model.getBirthday();
                    String formattedBirth = Utils.formatDateFromTo(birth,
                            "yyyy-MM-dd", "yyyy/MM/dd");
                    gender = model.getGender();
                    registerModel.setId(String.valueOf(model.getId()));
                    registerModel.setUserCode(model.getUserCode());
                    registerModel.setBirthday(formattedBirth);
                    registerModel.setFirstName(model.getFirstName());
                    registerModel.setLastName(model.getLastName());
                    registerModel.setFirstNickName(model.getFirstNickName());
                    registerModel.setLastNickName(model.getLastNickName());
                    registerModel.setNote(model.getNote());
                    registerModel.setUserCode(userCode);

                    registerModel.setReservationDate(formattedDate);
                    if (gender != 0 && gender != 1) {
                        registerModel.setGender(1);
                    } else {
                        registerModel.setGender(model.getGender());
                    }

                    tvUserCode.setText(userCode);
                    tvFirstName.setText(model.getFirstName());
                    tvLastName.setText(model.getLastName());
                    tvFirstNickName.setText(model.getFirstNickName());
                    tvLastNickName.setText(model.getLastNickName());
                    tvBirthday.setText(formattedBirth);
                    tvNote.setText(model.getNote());
                    tvAppointmentDay.setText(formattedDate);
                    if (gender == 1) {
                        rdMale.setChecked(true);
                    } else {
                        rdFemale.setChecked(true);
                    }
                }
                break;
            case AppConstants.ID_LAST_NICKNAME:
                if (response != null) {
                    JsonReader reader = new JsonReader(new StringReader(response));
                    reader.setLenient(true);
                    String kanjiResponse = gson.fromJson(reader, String.class);
                    tvLastNickName.setText(kanjiResponse);
                    registerModel.setLastNickName(kanjiResponse);
                    Log.e(TAG, kanjiResponse);
                }
                break;

            case AppConstants.ID_FIRST_NICKNAME:
                if (response != null) {
                    JsonReader reader = new JsonReader(new StringReader(response));
                    reader.setLenient(true);
                    String kanjiResponse = gson.fromJson(reader, String.class);
                    tvFirstNickName.setText(kanjiResponse);
                    registerModel.setFirstNickName(kanjiResponse);
                    Log.e(TAG, kanjiResponse);
                }
                break;
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {
        switch (idRequest) {
            case AppConstants.ID_SAVE_USER:
                Toast.makeText(getActivity(), "Hu hu hu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_male:
                gender = 1;
                break;
            case R.id.rb_female:
                gender = 0;
                break;
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
                case R.id.tv_user_id:
                    registerModel.setUserCode(text);
                    break;
                case R.id.tv_last_name:
                    registerModel.setLastName(text);

                    if (!wanaKana.isHiragana(text)) {
                        toHiragana(text, ID_LAST_NICKNAME);
                    } else {
                        tvLastNickName.setText(text);
                        registerModel.setLastNickName(text);
                    }
                    if (!validateTextInput(text)) {
                        tvLastName.setError(getString(R.string.error));
                        tvLastName.startAnimation(shake);
                    }
                    break;
                case R.id.tv_first_name:
                    registerModel.setFirstName(text);
                    if (!wanaKana.isHiragana(text)) {
                        toHiragana(text, ID_FIRST_NICKNAME);
                    } else {
                        tvFirstNickName.setText(text);
                        registerModel.setFirstNickName(text);
                    }
                    if (!validateTextInput(text)) {
                        tvFirstName.setError(getString(R.string.error));
                        tvFirstName.startAnimation(shake);
                    }
                    break;
                case R.id.tv_last_nickname:
                    //wkjT.bind();
                    registerModel.setLastNickName(text);
                    if (!validateTextInput(text)) {
                        tvLastNickName.setError(getString(R.string.error));
                        tvLastNickName.startAnimation(shake);
                    }
                    break;
                case R.id.tv_first_nickname:
                    registerModel.setFirstNickName(text);
                    if (!validateTextInput(text)) {
                        tvFirstNickName.setError(getString(R.string.error));
                        tvFirstNickName.startAnimation(shake);
                    }
                    break;
                case R.id.tv_note:
                    registerModel.setNote(text);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean validation() {
        boolean isValidate = true;

        if (TextUtils.isEmpty(registerModel.getUserCode())) {
            tvUserCode.startAnimation(shake);
            tvUserCode.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(registerModel.getFirstName())) {
            tvFirstName.startAnimation(shake);
            tvFirstName.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(registerModel.getLastName())) {
            tvLastName.startAnimation(shake);
            tvLastName.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(registerModel.getFirstNickName())) {
            tvFirstNickName.startAnimation(shake);
            tvFirstNickName.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(registerModel.getLastNickName())) {
            tvLastNickName.startAnimation(shake);
            tvLastNickName.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(registerModel.getBirthday())) {
            tvBirthday.startAnimation(shake);
            tvBirthday.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(registerModel.getNote())) {
            tvNote.startAnimation(shake);
            tvNote.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(registerModel.getReservationDate())) {
            tvAppointmentDay.startAnimation(shake);
            tvAppointmentDay.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        return isValidate;
    }

    private boolean validateTextInput(String text) {
        return text.matches(isHiraganaRegEx) || text.matches(isJapaneseRegEx);
    }

    private void toHiragana(String sentence, int id) {
        if (!TextUtils.isEmpty(sentence)) {
            kanjiNetworkManager.requestInstagramApi(
                    kanjiNetworkManager.toHiragana(sentence), id);
        }
    }

    private void resetInfo() {
        tvUserCode.setText("");
        tvLastName.setText("");
        tvFirstName.setText("");
        tvFirstNickName.setText("");
        tvLastNickName.setText("");
        tvNote.setText("");
        tvBirthday.setText("");
        tvAppointmentDay.setText("");

        registerModel = new RegisterModel();
    }

}
