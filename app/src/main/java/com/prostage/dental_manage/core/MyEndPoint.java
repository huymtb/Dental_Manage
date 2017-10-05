package com.prostage.dental_manage.core;

import com.google.gson.JsonObject;
import com.prostage.dental_manage.core.model.ResponseModel;
import com.prostage.dental_manage.core.model.ResultModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by congnc on 5/15/17.
 */

public interface MyEndPoint {
    @GET("/getTokenAdmin")
    Call<ResponseModel> getTokenAdmin();

    @GET("/reservations/getAllReservationsByDay/{date}")
    Call<ResponseModel> getAllReservationsByDay(@Path("date") String date);

    @GET("/usersInfo/all/{id}")
    Call<ResponseModel> getAllUserByAdmin(@Path("id") int id);

    @GET("/usersInfo/{id}")
    Call<ResponseModel> getUserInfoById(@Path("id") int id);

    @POST("/usersInfo")
    Call<ResultModel> saveUserInfo(@Body JsonObject data);

    @GET("/adminsInfo/{id}")
    Call<ResponseModel> getAdminInfoById(@Path("id") int id);

    @POST("/adminsInfo/save")
    Call<ResultModel> saveAdminInfo(@Body JsonObject data);
}
