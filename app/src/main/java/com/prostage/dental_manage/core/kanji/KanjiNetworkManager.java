package com.prostage.dental_manage.core.kanji;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.prostage.dental_manage.BuildConfig;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.core.IHttpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by congnc on 7/6/17.
 */

public class KanjiNetworkManager {
    private static Retrofit retrofit = null;
    Context mContext;
    IHttpResponse iHttpResponse;
    KanjiEndpoint apiService;

    public KanjiNetworkManager(Context context, IHttpResponse iHttpResponse) {
        try {
            this.iHttpResponse = iHttpResponse;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "IHttpResponse");
        }
        this.mContext = context;
        apiService = getClient().create(KanjiEndpoint.class);
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://labs.goo.ne.jp/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public void requestInstagramApi(Call<KanjiResponse> call, final int idRequest) {
        call.enqueue(new Callback<KanjiResponse>() {
            @Override
            public void onResponse(Call<KanjiResponse> call, Response<KanjiResponse> response) {
                KanjiResponse responseModel = response.body();
                if (responseModel != null) {
                    iHttpResponse.onHttpComplete(responseModel.getConverted(), idRequest);
                } else {
                    iHttpResponse.onHttpError("", idRequest, 0);
                }
            }

            @Override
            public void onFailure(Call<KanjiResponse> call, Throwable t) {
                Toast.makeText(mContext, mContext.getString(R.string.unknown_error_network), Toast.LENGTH_LONG).show();
            }
        });
    }

    public Call<KanjiResponse> toHiragana(String sentence) {
        String app_id = BuildConfig.LAB_API_KEY;
        JsonObject data = new JsonObject();
        data.addProperty("app_id", app_id);
        data.addProperty("sentence", sentence);
        data.addProperty("output_type", "hiragana");

        return apiService.toHiragana(data);
    }
}
