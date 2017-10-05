package com.prostage.dental_manage.utils;

/**
 * Created by congnc on 4/5/17.
 */

public final class AppConstants {
    private AppConstants() {
        //no instance
    }

    public static final int ID_FRAGMENT_TODAY = 0;
    public static final int ID_FRAGMENT_TOMORROW = 1;
    public static final int ID_FRAGMENT_NEXT_DAY = 2;

    public static final long NULL_INDEX = -1L;
    public static final String FIRE_URL = "https://dentalapp-ada99.firebaseio.com/";
    public static final String ARG_USERS = "users";
    public static final String ARG_CHAT_ROOMS = "chat_data";
    public static final String ARG_MY_FIREBASE_TOKEN = "firebaseToken";
    public static final int MIN_AGE = 14;
    public static final String ARG_IMAGE = "dental-image";
    public static final String NOTI_DATA = "notification";
    public static final String KEY_DATA = "KEY_DATA";
    public static final String KEY_DATA_TWO = "KEY_DATA_TWO";
    public static final String KEY_DATA_THREE = "KEY_DATA_THREE";

    public static final int ID_LOGIN = 0;
    public static final int ID_RESERVATION = 1;
    public static final int ID_LIST_CHAT = 2;
    public static final int ID_ADMIN = 3;
    public static final int ID_SAVE_USER = 4;
    public static final int ID_UPDATE_USER = 5;
    public static final int ID_USER_INFO = 6;
    public static final int ID_SAVE_ADMIN = 7;
    public static final int ID_USER_LIST = 8;
    public static final int ID_HIRAGANA = 9;
    public static final int ID_LAST_NICKNAME = 10;
    public static final int ID_FIRST_NICKNAME = 11;

    public static final int TAG_TIME_FIRST_NOTIFY = 0;
    public static final int TAG_TIME_SECOND_NOTIFY = 1;
    public static final int TAG_CONTENT_FIRST_NOTIFY = 3;
    public static final int TAG_CONTENT_SECOND_NOTIFY = 4;
    public static final int TAG_ADDRESS = 5;
    public static final int TAG_TEL = 6;
    public static final int TAG_CLOSED_DAY = 7;
    public static final int TAG_CLINIC_ITEM = 8;
    public static final int TAG_PRACTICE_REMARK = 9;
    public static final int TAG_REMARK = 10;
}
