package com.prostage.dental_manage.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnc on 5/16/17.
 */

public class WorkingDayModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("idWorkingDay")
    @Expose
    private Integer idWorkingDay;
    @SerializedName("firstShiftFromHour")
    @Expose
    private Integer firstShiftFromHour;
    @SerializedName("firstShiftFromMin")
    @Expose
    private Integer firstShiftFromMin;
    @SerializedName("firstShiftToHour")
    @Expose
    private Integer firstShiftToHour;
    @SerializedName("firstShiftToMin")
    @Expose
    private Integer firstShiftToMin;
    @SerializedName("secondShiftFromHour")
    @Expose
    private Integer secondShiftFromHour;
    @SerializedName("secondShiftFromMin")
    @Expose
    private Integer secondShiftFromMin;
    @SerializedName("secondShiftToHour")
    @Expose
    private Integer secondShiftToHour;
    @SerializedName("secondShiftToMin")
    @Expose
    private Integer secondShiftToMin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdWorkingDay() {
        return idWorkingDay;
    }

    public void setIdWorkingDay(Integer idWorkingDay) {
        this.idWorkingDay = idWorkingDay;
    }

    public Integer getFirstShiftFromHour() {
        return firstShiftFromHour;
    }

    public void setFirstShiftFromHour(Integer firstShiftFromHour) {
        this.firstShiftFromHour = firstShiftFromHour;
    }

    public Integer getFirstShiftFromMin() {
        return firstShiftFromMin;
    }

    public void setFirstShiftFromMin(Integer firstShiftFromMin) {
        this.firstShiftFromMin = firstShiftFromMin;
    }

    public Integer getFirstShiftToHour() {
        return firstShiftToHour;
    }

    public void setFirstShiftToHour(Integer firstShiftToHour) {
        this.firstShiftToHour = firstShiftToHour;
    }

    public Integer getFirstShiftToMin() {
        return firstShiftToMin;
    }

    public void setFirstShiftToMin(Integer firstShiftToMin) {
        this.firstShiftToMin = firstShiftToMin;
    }

    public Integer getSecondShiftFromHour() {
        return secondShiftFromHour;
    }

    public void setSecondShiftFromHour(Integer secondShiftFromHour) {
        this.secondShiftFromHour = secondShiftFromHour;
    }

    public Integer getSecondShiftFromMin() {
        return secondShiftFromMin;
    }

    public void setSecondShiftFromMin(Integer secondShiftFromMin) {
        this.secondShiftFromMin = secondShiftFromMin;
    }

    public Integer getSecondShiftToHour() {
        return secondShiftToHour;
    }

    public void setSecondShiftToHour(Integer secondShiftToHour) {
        this.secondShiftToHour = secondShiftToHour;
    }

    public Integer getSecondShiftToMin() {
        return secondShiftToMin;
    }

    public void setSecondShiftToMin(Integer secondShiftToMin) {
        this.secondShiftToMin = secondShiftToMin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.idWorkingDay);
        dest.writeValue(this.firstShiftFromHour);
        dest.writeValue(this.firstShiftFromMin);
        dest.writeValue(this.firstShiftToHour);
        dest.writeValue(this.firstShiftToMin);
        dest.writeValue(this.secondShiftFromHour);
        dest.writeValue(this.secondShiftFromMin);
        dest.writeValue(this.secondShiftToHour);
        dest.writeValue(this.secondShiftToMin);
    }

    public WorkingDayModel() {
    }

    protected WorkingDayModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.idWorkingDay = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstShiftFromHour = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstShiftFromMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstShiftToHour = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstShiftToMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.secondShiftFromHour = (Integer) in.readValue(Integer.class.getClassLoader());
        this.secondShiftFromMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.secondShiftToHour = (Integer) in.readValue(Integer.class.getClassLoader());
        this.secondShiftToMin = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<WorkingDayModel> CREATOR = new Creator<WorkingDayModel>() {
        @Override
        public WorkingDayModel createFromParcel(Parcel source) {
            return new WorkingDayModel(source);
        }

        @Override
        public WorkingDayModel[] newArray(int size) {
            return new WorkingDayModel[size];
        }
    };
}
