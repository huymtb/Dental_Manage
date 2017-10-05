package com.prostage.dental_manage.core.kanji;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by congnc on 7/6/17.
 */

public interface KanjiEndpoint {
    @POST("api/hiragana")
    Call<KanjiResponse> toHiragana(@Body JsonObject data);
}
