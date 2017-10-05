package com.prostage.dental_manage.chat;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.prostage.dental_manage.utils.AppConstants.ARG_CHAT_ROOMS;

/**
 * Created by congnc on 7/19/17.
 */

public class MessageService extends IntentService {

    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String senderId = intent.getStringExtra("senderId");
        String receiverId = intent.getStringExtra("receiverId");
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference chatRoomRef = mDatabaseRef.child(ARG_CHAT_ROOMS)
                .child(senderId + "_" + receiverId);

        chatRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    if (!String.valueOf(value.child("senderId").getValue()).equals(String.valueOf(senderId))) {
                        String keyId = value.getKey();
                        if (!(boolean) value.child("read").getValue()) {
                            chatRoomRef.child(keyId).child("read").setValue(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
