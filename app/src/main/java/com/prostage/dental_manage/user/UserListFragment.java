package com.prostage.dental_manage.user;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.TitleBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends BaseFragment implements TitleBar.OnTitleBarListener,
        UserListAdapter.OnUserListAdapterListener, IHttpResponse {
    public static final String TAG = UserListFragment.class.getSimpleName();
    private TitleBar titleBar;
    private RecyclerView rcvListUser;
    private UserListAdapter userListAdapter;
    private ArrayList<UserModel> userModelList = new ArrayList<>();
    NetworkManager networkManager;
    int adminId;

    public static UserListFragment newInstance() {

        Bundle args = new Bundle();

        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getActivity(), this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestModels = new ArrayList<>();
        //requestModels = RequestModel.createData();
        userListAdapter = new UserListAdapter(getActivity(), userModelList);
        adminId = Utils.getInt(getActivity(), Utils.PREF_ADMIN_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = view.findViewById(R.id.title_bar);
        rcvListUser = view.findViewById(R.id.rcv_list_user);

        if (userModelList.size() == 0) {
            networkManager.requestApi(networkManager.getAllUserByAdmin(adminId), AppConstants.ID_RESERVATION);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.title_user_list));
        titleBar.setIconBack(R.mipmap.ic_back);
        titleBar.setListener(this);
        rcvListUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvListUser.setHasFixedSize(true);
        userListAdapter.setListener(this);
        rcvListUser.setAdapter(userListAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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

    @Override
    public void onChatItemClicked(UserModel userModel) {
        Utils.hideSoftKeyboard(getActivity());
        MessageFragment fragment = MessageFragment.newInstance(String.valueOf(userModel.getUserCode()),
                adminId + "_" + userModel.getUserCode(), userModel.getFirstName(), userModel.getGender());
        add(fragment, MessageFragment.TAG, true, true, R.id.flContainer);
    }

    @Override
    public void onUserClicked(UserModel userModel) {

    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();
        switch (idRequest) {
            case AppConstants.ID_RESERVATION:
                Type type = new TypeToken<List<UserModel>>() {
                }.getType();
                userModelList = gson.fromJson(response, type);
                userListAdapter.setList(userModelList);
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }
}
