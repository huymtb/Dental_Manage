package com.prostage.dental_manage.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnc on 5/16/17.
 */

public class AdminLoginModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("permission")
    @Expose
    private String permission;
    @SerializedName("accountNonExpired")
    @Expose
    private Boolean accountNonExpired;
    @SerializedName("accountNonLocked")
    @Expose
    private Boolean accountNonLocked;
    @SerializedName("credentialsNonExpired")
    @Expose
    private Boolean credentialsNonExpired;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.permission);
        dest.writeValue(this.accountNonExpired);
        dest.writeValue(this.accountNonLocked);
        dest.writeValue(this.credentialsNonExpired);
        dest.writeValue(this.enabled);
    }

    public AdminLoginModel() {
    }

    protected AdminLoginModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.username = in.readString();
        this.password = in.readString();
        this.permission = in.readString();
        this.accountNonExpired = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.accountNonLocked = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.credentialsNonExpired = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.enabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<AdminLoginModel> CREATOR = new Creator<AdminLoginModel>() {
        @Override
        public AdminLoginModel createFromParcel(Parcel source) {
            return new AdminLoginModel(source);
        }

        @Override
        public AdminLoginModel[] newArray(int size) {
            return new AdminLoginModel[size];
        }
    };
}
