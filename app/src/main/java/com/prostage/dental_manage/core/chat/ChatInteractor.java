package com.prostage.dental_manage.core.chat;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prostage.dental_manage.base.fcm.FcmNotificationBuilder;
import com.prostage.dental_manage.core.model.FileModel;
import com.prostage.dental_manage.core.model.MessageModel;
import com.prostage.dental_manage.utils.AppConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by congnc on 4/7/17.
 */

public class ChatInteractor implements ChatContract.Interactor {
    private static final String TAG = "ChatInteractor";

    private ChatContract.OnSendMessageListener mOnSendMessageListener;
    private ChatContract.OnGetMessagesListener mOnGetMessagesListener;

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public ChatInteractor(ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener,
                          ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    @Override
    public void sendMessageToFirebase(Context context, final MessageModel messageModel, final String receiverId) {
        final String chat_room = messageModel.getSenderId() + "_" + receiverId;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(AppConstants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(chat_room)) {
                    Log.e(TAG, "sendMessageToFirebase: " + chat_room + " exists");
                    databaseReference.child(AppConstants.ARG_CHAT_ROOMS).child(chat_room)
                            .push().setValue(messageModel);
                } else {
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                    databaseReference.child(AppConstants.ARG_CHAT_ROOMS).child(chat_room)
                            .push().setValue(messageModel);

                    getMessageFromFirebase(messageModel.getSenderId(), receiverId);
                }
                FcmNotificationBuilder.initialize()
                        .title(messageModel.getSenderId())
                        .message(messageModel.getText())
                        .topic(messageModel.getSenderId())
                        .sender(messageModel.getSenderId())
                        .send();
                mOnSendMessageListener.onSendMessageSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotificationToReceiver(String username,
                                                String message,
                                                String topic,
                                                String senderId) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .topic(topic)
                .sender(senderId)
                .read(false)
                .send();
    }


    @Override
    public void getMessageFromFirebase(String senderId, String receiverId) {
        final String room = senderId + "_" + receiverId;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(AppConstants.ARG_CHAT_ROOMS).getRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(room)) {
                            Log.e(TAG, "getMessageFromFirebaseUser: " + room + " exists");
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(AppConstants.ARG_CHAT_ROOMS)
                                    .child(room).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                                    mOnGetMessagesListener.onGetMessagesSuccess(messageModel);
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
                                    mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void sendImageToFirebase(Context context, final MessageModel messageModel,
                                    final Uri file, final String receiverId) {
//        final String chat_room = messageModel.getSenderId() + "_" + receiverId;
//        final DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
//        Date date = new Date();
//        final String name = messageModel.getSenderId() + "_" + dateFormat.format(date) + ".jpg";
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        final StorageReference imageGalleryRef = storageRef.child(AppConstants.ARG_IMAGE)
//                .child(name);
//
//        UploadTask uploadTask = imageGalleryRef.putFile(file);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                FileModel fileModel = new FileModel(name, downloadUrl.toString());
//                messageModel.setFile(fileModel);
//                databaseReference.child(AppConstants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChild(chat_room)) {
//                            Log.e(TAG, "sendMessageToFirebase: " + chat_room + " exists");
//                            databaseReference.child(AppConstants.ARG_CHAT_ROOMS).child(chat_room)
//                                    .push().setValue(messageModel);
//                        } else {
//                            Log.e(TAG, "sendMessageToFirebaseUser: success");
//                            databaseReference.child(AppConstants.ARG_CHAT_ROOMS).child(chat_room)
//                                    .push().setValue(messageModel);
//
//                        }
//                        FcmNotificationBuilder.initialize()
//                                .title(messageModel.getSenderId())
//                                .message(messageModel.getText())
//                                .topic(messageModel.getSenderId())
//                                .sender(messageModel.getSenderId())
//                                .send();
//                        getMessageFromFirebase(messageModel.getSenderId(), receiverId);
//                        mOnSendMessageListener.onSendMessageSuccess();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
//                    }
//                });
//            }
//        });
    }
}
