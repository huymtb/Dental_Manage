package com.prostage.dental_manage.core.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnc on 5/15/17.
 */

public class ResponseModel {
    @SerializedName("message")
    @Expose
    private JsonElement message;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("status")
    @Expose
    private Integer status;

    public JsonElement getMessage() {
        return message;
    }

    public void setMessage(JsonElement message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
