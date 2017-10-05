package com.prostage.dental_manage.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnc on 5/17/17.
 */

public class RegisterModel implements Parcelable {

    public RegisterModel(String id, String userCode, String birthday, String firstName,
                         String lastName, String firstNickName, String lastNickName,
                         String note, String reservationDate) {
        this.id = id;
        this.userCode = userCode;
        this.birthday = birthday;
        this.firstName = firstName;
        this.lastName = lastName;
        this.firstNickName = firstNickName;
        this.lastNickName = lastNickName;
        this.note = note;
        this.reservationDate = reservationDate;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("adminId")
    @Expose
    private Integer adminId;
    @SerializedName("userCode")
    @Expose
    private String userCode;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("firstNickName")
    @Expose
    private String firstNickName;
    @SerializedName("lastNickName")
    @Expose
    private String lastNickName;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("reservationDate")
    @Expose
    private String reservationDate;
    @SerializedName("gender")
    @Expose
    private int gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstNickName() {
        return firstNickName;
    }

    public void setFirstNickName(String firstNickName) {
        this.firstNickName = firstNickName;
    }

    public String getLastNickName() {
        return lastNickName;
    }

    public void setLastNickName(String lastNickName) {
        this.lastNickName = lastNickName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public RegisterModel() {
        this.setId("");
        this.setAdminId(-1);
        this.setFirstName("");
        this.setLastName("");
        this.setFirstNickName("");
        this.setLastNickName("");
        this.setBirthday("");
        this.setNote("");
        this.setReservationDate("");
        this.setGender(1);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeValue(this.adminId);
        dest.writeString(this.userCode);
        dest.writeString(this.birthday);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.firstNickName);
        dest.writeString(this.lastNickName);
        dest.writeString(this.note);
        dest.writeString(this.reservationDate);
        dest.writeInt(this.gender);
    }

    protected RegisterModel(Parcel in) {
        this.id = in.readString();
        this.adminId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userCode = in.readString();
        this.birthday = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.firstNickName = in.readString();
        this.lastNickName = in.readString();
        this.note = in.readString();
        this.reservationDate = in.readString();
        this.gender = in.readInt();
    }

    public static final Creator<RegisterModel> CREATOR = new Creator<RegisterModel>() {
        @Override
        public RegisterModel createFromParcel(Parcel source) {
            return new RegisterModel(source);
        }

        @Override
        public RegisterModel[] newArray(int size) {
            return new RegisterModel[size];
        }
    };
}
