package com.prostage.dental_manage.calendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.prostage.dental_manage.DentalManageApp;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.authentication.AuthenticationActivity;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.base.EventDistributor;
import com.prostage.dental_manage.base.QuestionDialog;
import com.prostage.dental_manage.chat.MessageFragment;
import com.prostage.dental_manage.core.IHttpResponse;
import com.prostage.dental_manage.core.NetworkManager;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.register.UpdateUserFragment;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.TitleBar;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_ID;
import static com.prostage.dental_manage.utils.Utils.hideSoftKeyboard;

public class ReservationFragment extends BaseFragment implements TitleBar.OnTitleBarListener,
        QuestionDialog.QuestionRequestDialogDelegate, IHttpResponse, View.OnClickListener,
        DatePickerDialog.OnDateSetListener, ReservationAdapter.OnReservationAdapterListener {

    public static final String TAG = "SchedulePageFragment";

    private static final int EVENTS = EventDistributor.UPDATE_NEW_RESERVATION;

    private TitleBar titleBar;

    View emptyList;
    NetworkManager networkManager;
    int adminId;

    private RecyclerView rcvRequest;
    private ReservationAdapter reservationAdapter;
    private ArrayList<UserModel> reservationModels;
    private ArrayList<UserModel> listAll;
    private ImageView ivPrev;
    private ImageView ivNext;
    private TextView tvDate;
    //private ProgressBar progLoading;

    Calendar cal;
    Calendar now;
    int year;
    int month;
    int day;
    Date choseDate;
    Date nowDate;
    SimpleDateFormat df;
    SimpleDateFormat sdf;
    String formattedDate;
    String serverDate;
    DatePickerDialog dpd;


    public static ReservationFragment newInstance() {
        Bundle args = new Bundle();
        ReservationFragment fragment = new ReservationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(context, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservations, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = view.findViewById(R.id.title_bar);
        rcvRequest = view.findViewById(R.id.list_reservations);
        ivPrev = view.findViewById(R.id.iv_prev);
        ivNext = view.findViewById(R.id.iv_next);
        tvDate = view.findViewById(R.id.tv_date);
        emptyList = view.findViewById(R.id.layout_empty);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.title_calendar));
        titleBar.addIconRight();
        titleBar.setIconBack(R.mipmap.ic_refresh);
        titleBar.setListener(this);

        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        tvDate.setOnClickListener(this);

        cal = Calendar.getInstance();
        choseDate = cal.getTime();
        now = Calendar.getInstance();
        nowDate = now.getTime();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        df = new SimpleDateFormat("yyyy年M月dd日", Locale.US);
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formattedDate = df.format(cal.getTime());
        serverDate = sdf.format(cal.getTime());
        tvDate.setText(formattedDate);

        adminId = Utils.getInt(getActivity(), PREF_ADMIN_ID);
        rcvRequest.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (listAll == null) {
            listAll = new ArrayList<>();
            getListUser();
        } else {
            if (reservationModels == null) {
                reservationModels = new ArrayList<>();
            }
            reservationModels.clear();
            reservationModels.addAll(getListForDay(listAll, serverDate));
            handleResponse();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        EventDistributor.getInstance().register(contentUpdate);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventDistributor.getInstance().unregister(contentUpdate);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClickBackTitleBar() {
        hideSoftKeyboard(getActivity());
        getListUser();
    }

    @Override
    public void onClickRightIcon() {
        QuestionDialog dialog = new QuestionDialog(getActivity(), getString(R.string.log_out),
                getString(R.string.question_log_out));
        dialog.setColorTitle(Color.RED);
        dialog.setTitleButtonOK(getString(R.string.ok));
        dialog.setDelegate(this, "");
        dialog.show();
    }

    @Override
    public void clickAllow(Boolean bAllow, String type) {
        if (bAllow) {
            Toast.makeText(getContext(), getString(R.string.you_are_logout), Toast.LENGTH_LONG).show();
            DentalManageApp.setToken("");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/topic_" + adminId);
            Utils.clear(getActivity());
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.KEY_DATA, "mainactivity");
            startNewActivity(AuthenticationActivity.class, bundle);
        }
    }

    private void handleResponse() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(this::updateData);
        }
    }

    private void updateData() {
        if (reservationModels != null && reservationModels.size() > 0) {
            emptyList.setVisibility(View.GONE);
            rcvRequest.setVisibility(View.VISIBLE);

            if (reservationAdapter == null) {
                reservationAdapter = new ReservationAdapter(getContext(), new ArrayList<>());
                reservationAdapter.setListener(this);
                rcvRequest.setAdapter(reservationAdapter);
            }
            if (reservationModels != null && reservationModels.size() > 0) {
                Collections.sort(reservationModels, (user1, user2) -> {
                    if (user1.getReservations().get(0).getReservationHour() > user2.getReservations().get(0).getReservationHour()) {
                        return 1;
                    } else if (Objects.equals(user1.getReservations().get(0).getReservationHour(), user2.getReservations().get(0).getReservationHour())) {
                        if (user1.getReservations().get(0).getReservationMin() >= user2.getReservations().get(0).getReservationMin()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                });
            }

            reservationAdapter.setList(reservationModels);
            reservationAdapter.notifyDataSetChanged();
        } else {
            emptyList.setVisibility(View.VISIBLE);
            rcvRequest.setVisibility(View.GONE);
        }

    }


    @Override
    public void onHttpComplete(String response, int idRequest) {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();
        switch (idRequest) {
            case AppConstants.ID_RESERVATION:
                Type type = new TypeToken<List<UserModel>>() {
                }.getType();
                if (response != null) {
                    listAll = gson.fromJson(response, type);
                    if (listAll != null && listAll.size() > 0) {
                        if (reservationModels == null) {
                            reservationModels = new ArrayList<>();
                        }
                        reservationModels.clear();
                        reservationModels.addAll(getListForDay(listAll, serverDate));

                        Log.e(TAG, "size: " + reservationModels.size());
                        for (int i = 0; i < reservationModels.size(); i++) {
                            Log.e(TAG, "on Reservations load: " + reservationModels.get(i).getId());
                        }
                    }
                    handleResponse();
                }
                break;
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }

    private ArrayList<UserModel> getListForDay(ArrayList<UserModel> listUser, String date) {
        ArrayList<UserModel> result = new ArrayList<>();
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getReservations().get(0).getReservationDay()
                    .equals(date)) {
                result.add(listUser.get(i));
            }
        }
        return result;
    }

    public void getListUser() {
        if (networkManager != null) {
            networkManager.requestApi(networkManager.getAllUserByAdmin(adminId), AppConstants.ID_RESERVATION);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_prev:
//                if (choseDate.after(nowDate)) {
                cal.add(Calendar.DATE, -1);
                formattedDate = df.format(cal.getTime());
                choseDate = cal.getTime();
                tvDate.setText(formattedDate);
                serverDate = sdf.format(cal.getTime());
                reservationModels = getListForDay(listAll, serverDate);
                handleResponse();
//                }
                break;

            case R.id.iv_next:
                cal.add(Calendar.DATE, 1);
                formattedDate = df.format(cal.getTime());
                choseDate = cal.getTime();
                tvDate.setText(formattedDate);
                serverDate = sdf.format(cal.getTime());
                reservationModels = getListForDay(listAll, serverDate);
                handleResponse();
                break;
            case R.id.tv_date:
                dpd = new DatePickerDialog(getActivity(), this, year, month, day);
                dpd.getDatePicker().setMinDate(now.getTimeInMillis());
                dpd.show();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        GregorianCalendar calendarBeg = new GregorianCalendar(view.getYear(), view.getMonth(), view.getDayOfMonth());
        cal = calendarBeg;
        choseDate = calendarBeg.getTime();
        formattedDate = df.format(choseDate);
        serverDate = sdf.format(choseDate);
        tvDate.setText(formattedDate);
        reservationModels = getListForDay(listAll, serverDate);
        handleResponse();
    }

    @Override
    public void onInfoItemClicked(UserModel userModel) {
        Utils.hideSoftKeyboard(getActivity());
//        RegisterFragment fragment = RegisterFragment.newInstance(userModel.getId(), userModel.getUserCode());
//        add(fragment, RegisterFragment.TAG_EDIT, true, true, R.id.flContainer);
        UpdateUserFragment fragment = UpdateUserFragment.newInstance(userModel, userModel.getId(), userModel.getUserCode());
        add(fragment, UpdateUserFragment.TAG, true, true, R.id.flContainer);
    }

    @Override
    public void onChatItemClicked(UserModel userModel) {
        Utils.hideSoftKeyboard(getActivity());
        MessageFragment fragment = MessageFragment.newInstance(String.valueOf(userModel.getUserCode()),
                adminId + "_" + userModel.getUserCode(), userModel.getFirstName(),
                userModel.getGender());
        add(fragment, MessageFragment.TAG, true, true, R.id.flContainer);
    }

    private EventDistributor.EventListener contentUpdate = new EventDistributor.EventListener() {
        @Override
        public void update(EventDistributor eventDistributor, Integer arg) {
            if ((arg & EVENTS) != 0) {
                Log.d(TAG, "arg: " + arg);
                getListUser();
            }
        }
    };
}
