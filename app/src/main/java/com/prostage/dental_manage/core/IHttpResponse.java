package com.prostage.dental_manage.core;

/**
 * Created by congnc on 5/15/17.
 */

public interface IHttpResponse {
    void onHttpComplete(String response, int idRequest);

    void onHttpError(String response, int idRequest, int errorCode);
}
