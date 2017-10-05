package com.prostage.dental_manage.base;

import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.prostage.dental_manage.DentalManageApp;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.authentication.AuthenticationFragment;
import com.prostage.dental_manage.core.model.AdminModel;
import com.prostage.dental_manage.core.IHttpResponse;
import com.prostage.dental_manage.core.NetworkManager;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.chat.MessageFragment;
import com.prostage.dental_manage.core.model.MessageModel;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.NonSwipeableViewPager;
import com.prostage.dental_manage.views.TabbarMain;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.prostage.dental_manage.chat.MessageFragment.CHAT_NAME;
import static com.prostage.dental_manage.utils.AppConstants.ID_USER_INFO;

public class MainActivity extends CommonActivity implements TabbarMain.OnTabbarMainListener,
        IHttpResponse {
    public static final String TAG = "MainActivity";
    private AdminModel adminModel;
    private String adminId;
    private TabbarMain tabbarMain;
    private NonSwipeableViewPager viewPager;
    private List<UserModel> userModelList = new ArrayList<>();
    NetworkManager networkManager;
    Gson gson = new Gson();

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkManager = new NetworkManager(this, this);
        viewPager = findViewById(R.id.main_viewpager);
        tabbarMain = findViewById(R.id.main_tabbar);
        adminId = String.valueOf(Utils.getInt(this, Utils.PREF_ADMIN_ID));
        adminModel = gson.fromJson(Utils.getString(this, Utils.PREF_ADMIN_DATA), AdminModel.class);
        FeaturesPagerAdapter featuresPagerAdapter = new FeaturesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(featuresPagerAdapter);
        tabbarMain.setListener(this);
        tabbarMain.changeTab(TabbarMain.CALENDAR_INDEX);

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/topic_" + adminId);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        checkMessageUnread();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.getExtras() != null) {
            MessageModel messageModel = intent.getExtras().getParcelable(AppConstants.NOTI_DATA);
            String senderId, receiverId, receiverName, chatroomId;
            senderId = String.valueOf(adminId);
            assert messageModel != null;
            receiverId = messageModel.getSenderId();
            chatroomId = senderId + "_" + receiverId;
            receiverName = intent.getStringExtra(CHAT_NAME);

            networkManager.requestApi(networkManager.getUserInfoById(Integer.parseInt(receiverId)), ID_USER_INFO);
            Log.e(TAG, "onNewIntent: co tin nhan ne ma");
            //checkMessage(receiverId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(AuthenticationFragment.ARG_USER, adminModel);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onChangeTabMain(final int index) {
        Utils.hideSoftKeyboard(this);
        runOnUiThread(() -> {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
//                        indexTab = index;
                if (viewPager != null) {
                    viewPager.setCurrentItem(index - 1, true);
                }
            }, 300);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DentalManageApp.setChatActivityOpen(true);
        checkMessageUnread();
        //stopService(new Intent(this, BackgroundService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        DentalManageApp.setChatActivityOpen(false);
        checkMessageUnread();
        //startService(new Intent(this, BackgroundService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        DentalManageApp.setChatActivityOpen(true);
        checkMessageUnread();
        //startService(new Intent(this, BackgroundService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void checkMessageUnread() {
        if (mDatabaseRef == null) {
            return;
        }
        mDatabaseRef.child(AppConstants.ARG_CHAT_ROOMS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int unread = 0;
                    for (DataSnapshot msg : dataSnapshot.getChildren()) {
                        String key = msg.getKey();
                        String ids = key.split("_")[0];
                        if (ids.contains(adminId)) {
                            for (DataSnapshot child : msg.getChildren()) {
                                String senderId = (String) child.child("senderId").getValue();
                                boolean read = (boolean) child.child("read").getValue();
                                if (!adminId.equals(senderId) && !read) {
                                    unread++;
                                }
                            }
                        }
                    }

                    if (tabbarMain != null) {
                        tabbarMain.setNumberChat(unread);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                if (models.size() > 0) {
                    userModelList.clear();
                    userModelList.addAll(models);
                }
                break;
            case ID_USER_INFO:
                UserModel model = gson.fromJson(response, UserModel.class);
                if (model != null) {
                    String chatroomId = adminId + "_" + model.getUserCode();
                    MessageFragment fragment = MessageFragment.newInstance(model.getUserCode(),
                            chatroomId, model.getFirstName(), model.getGender());
                    replace(fragment, MessageFragment.TAG, true, true);
                }
                break;
        }
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {

    }
}
