package com.prostage.dental_manage.core.chat;

import android.content.Context;
import android.net.Uri;

import com.prostage.dental_manage.core.model.MessageModel;

/**
 * Created by congnc on 4/7/17.
 */

public class ChatPresenter implements ChatContract.Presenter, ChatContract.OnSendMessageListener,
        ChatContract.OnGetMessagesListener {
    private ChatContract.View mView;
    private ChatInteractor mChatInteractor;

    public ChatPresenter(ChatContract.View view) {
        this.mView = view;
        mChatInteractor = new ChatInteractor(this, this);
    }

    @Override
    public void sendMessage(Context context, MessageModel chat, String receiverId) {
        mChatInteractor.sendMessageToFirebase(context, chat, receiverId);
    }

    @Override
    public void getMessage(String senderUid, String receiverUid) {
        mChatInteractor.getMessageFromFirebase(senderUid, receiverUid);
    }

    @Override
    public void sendImage(Context context, MessageModel messageModel, Uri file,  String receiverId) {
        mChatInteractor.sendImageToFirebase(context, messageModel, file, receiverId);
    }

    @Override
    public void onSendMessageSuccess() {
        mView.onSendMessageSuccess();
    }

    @Override
    public void onSendMessageFailure(String message) {
        mView.onSendMessageFailure(message);
    }

    @Override
    public void onGetMessagesSuccess(MessageModel chat) {
        mView.onGetMessagesSuccess(chat);
    }

    @Override
    public void onGetMessagesFailure(String message) {
        mView.onGetMessagesFailure(message);
    }
}
