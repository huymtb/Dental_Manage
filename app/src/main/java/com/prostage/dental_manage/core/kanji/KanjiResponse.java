package com.prostage.dental_manage.core.kanji;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnc on 7/6/17.
 */

public class KanjiResponse {
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("output_type")
    @Expose
    private String outputType;
    @SerializedName("converted")
    @Expose
    private String converted;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getConverted() {
        return converted;
    }

    public void setConverted(String converted) {
        this.converted = converted;
    }
}
