package com.prostage.dental_manage.core.chat;

import android.content.Context;
import android.net.Uri;

import com.prostage.dental_manage.core.model.MessageModel;

/**
 * Created by congnc on 4/7/17.
 */

public interface ChatContract {
    interface View {
        void onSendMessageSuccess();

        void onSendMessageFailure(String message);

        void onGetMessagesSuccess(MessageModel messageModel);

        void onGetMessagesFailure(String message);
    }

    interface Presenter {
        void sendMessage(Context context, MessageModel messageModel, String receiverId);

        void getMessage(String senderId, String receiverId);

        void sendImage(Context context, MessageModel messageModel, Uri file, String receiverId);
    }

    interface Interactor {
        void sendMessageToFirebase(Context context, MessageModel messageModel, String receiverId);

        void getMessageFromFirebase(String senderId, String receiverId);

        void sendImageToFirebase(Context context, MessageModel messageModel, Uri file, String receiverId);
    }

    interface OnSendMessageListener {
        void onSendMessageSuccess();

        void onSendMessageFailure(String message);
    }

    interface OnGetMessagesListener {
        void onGetMessagesSuccess(MessageModel messageModel);

        void onGetMessagesFailure(String message);
    }
}
