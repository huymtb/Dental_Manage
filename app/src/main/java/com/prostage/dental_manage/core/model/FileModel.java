package com.prostage.dental_manage.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by congnc on 4/7/17.
 */

public class FileModel implements Parcelable{
    String url;
    String name;

    public FileModel() {

    }

    public FileModel(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
    }

    protected FileModel(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
    }

    public static final Creator<FileModel> CREATOR = new Creator<FileModel>() {
        @Override
        public FileModel createFromParcel(Parcel source) {
            return new FileModel(source);
        }

        @Override
        public FileModel[] newArray(int size) {
            return new FileModel[size];
        }
    };
}
