package com.prostage.dental_manage.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.base.EventDistributor;
import com.prostage.dental_manage.core.model.AdminModel;
import com.prostage.dental_manage.core.IHttpResponse;
import com.prostage.dental_manage.core.NetworkManager;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.TitleBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_DATA;
import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_ID;

/**
 * Created by Linh on 3/30/2017.
 */

public class ChatFragment extends BaseFragment implements ChatAdapter.OnChatAdapterListener,
        IHttpResponse {
    public static final String TAG = ChatFragment.class.getSimpleName();
    private static final int EVENTS = EventDistributor.UPDATE_NEW_CHAT;
    private RecyclerView rcvChat;
    private ChatAdapter chatAdapter;
    private TitleBar titleBar;
    private RadioButton rbSortByEarly;
    private int adminId;
    private ArrayList<UserModel> userlist = new ArrayList<>();
    NetworkManager networkManager;
    Gson gson = new Gson();

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();
        ChatFragment fragment = new ChatFragment();
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
        adminId = Utils.getInt(getActivity(), PREF_ADMIN_ID);
        chatAdapter = new ChatAdapter(getActivity(), userlist);
        //adminModel = gson.fromJson(Utils.getString(getActivity(), PREF_ADMIN_DATA), AdminModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        //rdgSortGroup = (RadioGroup) view.findViewById(R.id.rg_sort_group);
        rbSortByEarly = (RadioButton) view.findViewById(R.id.rb_sort_by_early);
        //rbSortByUnread = (RadioButton) view.findViewById(R.id.rb_sort_by_unread);
        rcvChat = (RecyclerView) view.findViewById(R.id.rcv_list_chat);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.title_chat));
        rbSortByEarly.setChecked(true);
        rcvChat.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (userlist == null || userlist.size() == 0) {
            networkManager.requestApi(networkManager.getAllUserByAdmin(adminId),
                    AppConstants.ID_LIST_CHAT);
        }
        rcvChat.setAdapter(chatAdapter);
        chatAdapter.setListener(this);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onShowMessage(String receiverId, String chatRoomId, String name, int unreadCount, int gender) {
        Utils.hideSoftKeyboard(getActivity());

        MessageFragment fragment = MessageFragment.newInstance(String.valueOf(receiverId), chatRoomId, name, gender);
        add(fragment, ChatFragment.TAG, true, true, R.id.flContainer);
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();
        switch (idRequest) {
            case AppConstants.ID_LIST_CHAT:
                Type type = new TypeToken<List<UserModel>>() {
                }.getType();
                List<UserModel> models = gson.fromJson(response, type);
                if (models != null && models.size() > 0) {
                    chatAdapter.setList((ArrayList<UserModel>) models);
                }
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }

    private EventDistributor.EventListener contentUpdate = new EventDistributor.EventListener() {
        @Override
        public void update(EventDistributor eventDistributor, Integer arg) {
            if ((arg & EVENTS) != 0) {
                Log.d(TAG, "arg: " + arg);
                networkManager.requestApi(networkManager.getAllUserByAdmin(adminId),
                        AppConstants.ID_LIST_CHAT);
            }
        }
    };
}
