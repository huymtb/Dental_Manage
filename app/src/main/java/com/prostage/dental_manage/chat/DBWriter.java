package com.prostage.dental_manage.chat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.prostage.dental_manage.utils.AppConstants.ARG_CHAT_ROOMS;

/**
 * Created by congnguyen on 9/1/17.
 */

public class DBWriter {
    private static final String TAG = "DBWriter";

    private static final ExecutorService dbExec;

    static {
        dbExec = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });
    }

    private DBWriter() {
    }

    public static Future<?> readMessage(String senderId, String receiverId){
        return dbExec.submit(() -> {
            //String senderId = intent.getStringExtra("senderId");
            //String receiverId = intent.getStringExtra("receiverId");
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
        });
    }
}
