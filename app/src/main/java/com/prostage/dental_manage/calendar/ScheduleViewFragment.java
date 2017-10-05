package com.prostage.dental_manage.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.core.IHttpResponse;
import com.prostage.dental_manage.core.NetworkManager;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.chat.MessageFragment;
import com.prostage.dental_manage.register.RegisterFragment;
import com.prostage.dental_manage.register.UpdateUserFragment;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.prostage.dental_manage.utils.AppConstants.KEY_DATA;
import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_ID;

public class ScheduleViewFragment extends BaseFragment implements
        ReservationAdapter.OnReservationAdapterListener, IHttpResponse, View.OnClickListener {
    public static final String TAG = "CalendarDayFragment";
    private RecyclerView rcvRequest;
    private ReservationAdapter reservationAdapter;
    private ArrayList<UserModel> reservationModels;
    View emptyList;
    NetworkManager networkManager;
    int adminId;
    int type;

    public static ScheduleViewFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(KEY_DATA, id);
        ScheduleViewFragment fragment = new ScheduleViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getContext(), this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_day, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvRequest = (RecyclerView) view.findViewById(R.id.rcv_list_appointment);
        emptyList = view.findViewById(R.id.layout_empty);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rcvRequest.setLayoutManager(new LinearLayoutManager(getActivity()));

        type = getArguments().getInt(KEY_DATA);
        if (reservationModels == null) {
            reservationModels = new ArrayList<>();
            reservationAdapter = new ReservationAdapter(getActivity(), reservationModels);
            reservationAdapter.setListener(this);
            rcvRequest.setAdapter(reservationAdapter);
        }

        adminId = Utils.getInt(getActivity(), PREF_ADMIN_ID);

        getListUser();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onInfoItemClicked(UserModel userModel) {
        Utils.hideSoftKeyboard(getActivity());

        UpdateUserFragment fragment = UpdateUserFragment.newInstance(userModel, userModel.getId(), userModel.getUserCode());
        replace(fragment, UpdateUserFragment.TAG, true, true, R.id.flContainer);
//        RegisterFragment fragment = RegisterFragment.newInstance(userModel.getId(), userModel.getUserCode());
//        replace(fragment, RegisterFragment.TAG_EDIT, true, true, R.id.flContainer);
    }

    @Override
    public void onChatItemClicked(UserModel userModel) {
        Utils.hideSoftKeyboard(getActivity());
        MessageFragment fragment = MessageFragment.newInstance(String.valueOf(userModel.getUserCode()),
                adminId + "_" + userModel.getUserCode(), userModel.getFirstName(), userModel.getGender());
        replace(fragment, MessageFragment.TAG, true, true, R.id.flContainer);
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();
        switch (idRequest) {
            case AppConstants.ID_RESERVATION:
                Type type = new TypeToken<List<UserModel>>() {
                }.getType();
                if (reservationModels == null) {
                    reservationModels = new ArrayList<>();
                }
                ArrayList<UserModel> listAll = gson.fromJson(response, type);
                if (listAll != null) {
                    String date = getStringDateReservation(this.type);
                    reservationModels.clear();
                    reservationModels.addAll(getListForDay(listAll, date));
                    reservationAdapter.setListener(this);
                    reservationAdapter.notifyDataSetChanged();
                }
                setVisibleStatusEmptyList();
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }

    private void setVisibleStatusEmptyList() {
        if (reservationModels != null && reservationModels.size() > 0) {
            emptyList.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.VISIBLE);
        }
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

    private String getStringDateReservation(int type) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date nextday = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        SimpleDateFormat scheduleFormat = new SimpleDateFormat("M月dd日", Locale.US);

        switch (type) {
            case 0:
                return sdf.format(today);
            case 1:
                return sdf.format(tomorrow);
            case 2:
                return sdf.format(nextday);
            default:
                return null;
        }
    }

    public void getListUser() {
        if (networkManager != null) {
            networkManager.requestApi(networkManager.getAllUserByAdmin(adminId), AppConstants.ID_RESERVATION);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
