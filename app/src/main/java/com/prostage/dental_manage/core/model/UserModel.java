package com.prostage.dental_manage.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by congnc on 5/16/17.
 */

public class UserModel implements Parcelable {
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("reservations")
    @Expose
    private List<ReservationModel> reservations = null;
    @SerializedName("firstNickName")
    @Expose
    private String firstNickName;
    @SerializedName("lastNickName")
    @Expose
    private String lastNickName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("updatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("userCode")
    @Expose
    private String userCode;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Integer getNumberUnread() {
        return numberUnread;
    }

    public void setNumberUnread(Integer numberUnread) {
        this.numberUnread = numberUnread;
    }

    @SerializedName("reservationHour")
    @Expose
    private Integer reservationHour;

    private Integer numberUnread;

    public Integer getReservationHour() {
        return reservationHour;
    }

    public void setReservationHour(Integer reservationHour) {
        this.reservationHour = reservationHour;
    }

    public Integer getReservationMin() {
        return reservationMin;
    }

    public void setReservationMin(Integer reservationMin) {
        this.reservationMin = reservationMin;
    }

    @SerializedName("reservationMin")
    @Expose
    private Integer reservationMin;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<ReservationModel> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationModel> reservations) {
        this.reservations = reservations;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.birthday);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.note);
        dest.writeString(this.createdDate);
        dest.writeTypedList(this.reservations);
        dest.writeString(this.firstNickName);
        dest.writeString(this.lastNickName);
        dest.writeValue(this.id);
        dest.writeString(this.updatedDate);
        dest.writeString(this.userCode);
        dest.writeInt(this.gender);
        dest.writeValue(this.reservationHour);
        dest.writeValue(this.numberUnread);
        dest.writeValue(this.reservationMin);
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.birthday = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.note = in.readString();
        this.createdDate = in.readString();
        this.reservations = in.createTypedArrayList(ReservationModel.CREATOR);
        this.firstNickName = in.readString();
        this.lastNickName = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.updatedDate = in.readString();
        this.userCode = in.readString();
        this.gender = in.readInt();
        this.reservationHour = (Integer) in.readValue(Integer.class.getClassLoader());
        this.numberUnread = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reservationMin = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
