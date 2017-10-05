package com.prostage.dental_manage;

import android.app.Application;
import android.text.TextUtils;

import com.prostage.dental_manage.base.EventDistributor;

/**
 * Created by congnc on 4/5/17.
 */

public class DentalManageApp extends Application {
    private static boolean sIsChatActivityOpen = false;
    private static String token;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        DentalManageApp.sIsChatActivityOpen = isChatActivityOpen;
    }

    public static String getToken() {
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        return token;
    }

    public static void setToken(String token) {
        DentalManageApp.token = token;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        EventDistributor.getInstance();
    }
}
