package com.prostage.dental_manage.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prostage.dental_manage.DentalManageApp;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.core.model.FileModel;
import com.prostage.dental_manage.core.model.MessageModel;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.TitleBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.app.Activity.RESULT_OK;
import static com.prostage.dental_manage.utils.AppConstants.ARG_CHAT_ROOMS;

public class MessageFragment extends BaseFragment implements View.OnClickListener,
        TitleBar.OnTitleBarListener {
    public static final String TAG = MessageFragment.class.getSimpleName();
    public static final String RECEIVER_ID = "receiverId";
    public static final String CHAT_ROOM_ID = "chatRoomId";
    public static final String CHAT_NAME = "chatName";
    public static final String GENDER = "gender";
    private TitleBar titleBar;
    private RecyclerView rcvListMessage;
    private MessageAdapter messageAdapter;
    private ImageView ivAdd;
    private ImageView ivKeyboard;
    private ImageView ivMic;
    private EmojiconEditText tvMessageSend;
    private EmojIconActions emojIconActions;
    private String message;
    private String senderId;
    private String receiverId;

    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference chatRef = firebaseRef.child(ARG_CHAT_ROOMS);

    private Uri picUri;

    public static final int REQUEST_IMAGE_CAPTURE = 100;
    public static final int REQUESR_IMAGE_GALLERY = 101;

    public static MessageFragment newInstance(String receiverId, String chatRoomId, String name, int gender) {

        Bundle args = new Bundle();
        args.putString(RECEIVER_ID, receiverId);
        args.putString(CHAT_ROOM_ID, chatRoomId);
        args.putString(CHAT_NAME, name);
        args.putInt(GENDER, gender);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = view.findViewById(R.id.title_bar);
        rcvListMessage = view.findViewById(R.id.rcv_list_message);
        ivAdd = view.findViewById(R.id.iv_add);
        ivKeyboard = view.findViewById(R.id.iv_keyboard);
        ImageView ivFace = view.findViewById(R.id.iv_face);
        ivMic = view.findViewById(R.id.iv_mic);
        tvMessageSend = view.findViewById(R.id.tv_input_message);
        emojIconActions = new EmojIconActions(getActivity(), view, tvMessageSend, ivFace);
        emojIconActions.ShowEmojIcon();
        emojIconActions.setIconsIds(R.mipmap.ic_keyboard, R.mipmap.ic_emoji);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        senderId = String.valueOf(Utils.getInt(getActivity(), Utils.PREF_ADMIN_ID));
        receiverId = getArguments().getString(RECEIVER_ID);
        String receiverName = getArguments().getString(CHAT_NAME);
        int gender = getArguments().getInt(GENDER);
        Log.e(TAG, "onActivityCreated: gender " + gender);

        ArrayList<MessageModel> messageModels = new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity(), messageModels, gender);

        //messageAdapter.setGender(gender);

        titleBar.setTitle(receiverName);
        titleBar.setIconBack(R.mipmap.ic_back);
        titleBar.setListener(this);
        rcvListMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvListMessage.setAdapter(messageAdapter);
        ivAdd.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        ivMic.setOnClickListener(this);

        getMessageData();
        tvMessageSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message = s.toString();
                if (TextUtils.isEmpty(s)) {
                    ivMic.setImageResource(R.mipmap.ic_mic);
                } else {
                    ivMic.setImageResource(R.mipmap.ic_send_chat);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emojIconActions.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });

    }

    private void getMessageData() {
        final Query chatQuery = chatRef.child(senderId + "_" + receiverId).limitToLast(50);
        chatQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                MessageFragment.this.addMessage(messageModel);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                Utils.hideSoftKeyboard(getActivity());
                chooseImage();
                break;
            case R.id.iv_keyboard:
                break;
            case R.id.iv_mic:
                Utils.hideSoftKeyboard(getActivity());
                if (!TextUtils.isEmpty(message)) {
                    final Handler handler = new Handler();
                    handler.postDelayed(this::sendMessage, 200);

                }
                break;
        }
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

    private void sendMessage() {
        final DatabaseReference chatRef = firebaseRef.child(ARG_CHAT_ROOMS).child(senderId + "_" + receiverId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
        Date date = new Date();
        String formattedDate = sdf.format(date);
        MessageModel model = new MessageModel();
        model.setText(message);
        model.setRead(false);
        model.setDate(formattedDate);
        model.setSenderId(senderId);

        chatRef.push().setValue(model).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(e -> Toast.makeText(MessageFragment.this.getActivity(),
                e.getMessage(), Toast.LENGTH_SHORT).show());
        message = "";
        tvMessageSend.setText("");
    }

    private void sendImage(final Uri file) {

        final DatabaseReference chatRef = firebaseRef.child(ARG_CHAT_ROOMS).child(senderId + "_" + receiverId);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS", Locale.US);
        Date date = new Date();
        String name = dateFormat.format(date);
        StorageReference imageGalleryRef = storageReference.child(senderId + "_" + name + ".jpg");
        UploadTask uploadTask = imageGalleryRef.putFile(file);
        uploadTask.addOnFailureListener(e -> {
        }).addOnSuccessListener(taskSnapshot -> {
            //dinh dang thoi gian gui len firebase
            DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            String dateModified = dateFormat1.format(new Date());

            //dinh dang ten file gui len firebase
            DateFormat dateFormatFile = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS", Locale.US);
            String nameFile = dateFormatFile.format(new Date());

            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            assert downloadUrl != null;
            FileModel fileModel = new FileModel(downloadUrl.toString(), senderId + "_" + nameFile + ".jpg");
            MessageModel model = new MessageModel();
            model.setSenderId(senderId);
            model.setDate(dateModified);
            model.setText("image");
            model.setRead(false);
            model.setFile(fileModel);
            chatRef.push().setValue(model);
        });
    }

    public void addMessage(MessageModel messageModel) {
        messageAdapter.add(messageModel);
        rcvListMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void chooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(getResources().getStringArray(R.array.choose_image), (dialog, which) -> {
            if (which == 0) {
                dispatchSelectImageIntent();
            } else {
                dispatchTakePictureIntent();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                picUri = FileProvider.getUriForFile(getContext(), "com.prostage.dental_manage.fileprovider", Utils.getOutputMediaFile(getContext()));
            } else {
                picUri = Uri.fromFile(Utils.getOutputMediaFile(getContext()));
            }
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, picUri);
            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchSelectImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUESR_IMAGE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Utils.hideSoftKeyboard(getActivity());
            final Handler handler = new Handler();
            handler.postDelayed(() -> sendImage(picUri), 200);

        } else if (requestCode == REQUESR_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            picUri = data.getData();
            final Handler handler = new Handler();
            handler.postDelayed(() -> sendImage(picUri), 200);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final Handler handler = new Handler();
        handler.postDelayed(() -> DBWriter.readMessage(senderId, receiverId), 3000);
        DentalManageApp.setChatActivityOpen(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        DentalManageApp.setChatActivityOpen(false);
    }
}
