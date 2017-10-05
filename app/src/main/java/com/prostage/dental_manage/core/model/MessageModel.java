package com.prostage.dental_manage.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by congnc on 3/30/17.
 */

public class MessageModel implements Parcelable{
    private String date;
    private boolean read;
    private String senderId;
    private String text;
    private FileModel file;

    public String getDate() {
        if (TextUtils.isEmpty(date)) {
            date = "";
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeByte(this.read ? (byte) 1 : (byte) 0);
        dest.writeString(this.senderId);
        dest.writeString(this.text);
        dest.writeParcelable(this.file, flags);
    }

    public MessageModel() {
    }

    protected MessageModel(Parcel in) {
        this.date = in.readString();
        this.read = in.readByte() != 0;
        this.senderId = in.readString();
        this.text = in.readString();
        this.file = in.readParcelable(FileModel.class.getClassLoader());
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
}
