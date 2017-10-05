package com.prostage.dental_manage.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnc on 5/16/17.
 */

public class ReservationModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("reservationDate")
    @Expose
    private String reservationDate;
    @SerializedName("reservationDay")
    @Expose
    private String reservationDay;
    @SerializedName("reservationHour")
    @Expose
    private Integer reservationHour;
    @SerializedName("reservationMin")
    @Expose
    private Integer reservationMin;
    @SerializedName("reservationSec")
    @Expose
    private Integer reservationSec;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;

    public ReservationModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationDay() {
        return reservationDay;
    }

    public void setReservationDay(String reservationDay) {
        this.reservationDay = reservationDay;
    }

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

    public Integer getReservationSec() {
        return reservationSec;
    }

    public void setReservationSec(Integer reservationSec) {
        this.reservationSec = reservationSec;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.reservationDate);
        dest.writeString(this.reservationDay);
        dest.writeValue(this.reservationHour);
        dest.writeValue(this.reservationMin);
        dest.writeValue(this.reservationSec);
        dest.writeString(this.status);
        dest.writeValue(this.createdBy);
    }

    protected ReservationModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reservationDate = in.readString();
        this.reservationDay = in.readString();
        this.reservationHour = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reservationMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reservationSec = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = in.readString();
        this.createdBy = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<ReservationModel> CREATOR = new Creator<ReservationModel>() {
        @Override
        public ReservationModel createFromParcel(Parcel source) {
            return new ReservationModel(source);
        }

        @Override
        public ReservationModel[] newArray(int size) {
            return new ReservationModel[size];
        }
    };
}
