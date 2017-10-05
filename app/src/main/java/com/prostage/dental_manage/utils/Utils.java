package com.prostage.dental_manage.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.prostage.dental_manage.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Linh on 3/29/2017.
 */

public class Utils {
    private static final String APP_PREFS = "APP_PREFS";
    public static final String PREF_ADMIN_ID = "PREF_ADMIN_ID";
    public static final String PREF_ADMIN_DATA = "PREF_ADMIN_DATA";
    public static final String PREF_USER_NAME = "PREF_USER_NAME";
    public static final String PREF_PASSWORD = "PREF_PASSWORD";
    public static final String PREF_REMEMBER = "PREF_REMEMBER";

    private Utils() {
        //no instance
    }

    public static void saveString(Context mContext, String key, String value) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static void saveInt(Context mContext, String key, int value) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public static void saveBoolean(Context mContext, String key, boolean value) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public static String getString(Context mContext, String key) {
        SharedPreferences mSharedPreferences =
                mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, null);
    }

    public static String getString(Context mContext, String key, String defaultValue) {
        SharedPreferences mSharedPreferences =
                mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, defaultValue);
    }

    public static int getInt(Context mContext, String key) {
        SharedPreferences mSharedPreferences =
                mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, 0);
    }

    public static int getInt(Context mContext, String key, int defaultValue) {
        SharedPreferences mSharedPreferences =
                mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public static boolean getBoolean(Context mContext, String key) {
        SharedPreferences mSharedPreferences =
                mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, false);
    }

    public static boolean getBoolean(Context mContext, String key, boolean defaultValue) {
        SharedPreferences mSharedPreferences =
                mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public static void clear(Context mContext) {
        SharedPreferences mSharedPreferences =
                mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        mSharedPreferences.edit().clear().apply();
    }

    public static void hideSoftKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    public static float dip2px(Context context, float dpValue) {
        if (context != null) {
            float scale = context.getResources().getDisplayMetrics().density;
            return dpValue * scale;
        }
        return 0;
    }

    public static int getColor(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    public static String formatDateLocal(Context context, Date date) {
        DateFormat outFormat = android.text.format.DateFormat.getDateFormat(context);
        return outFormat.format(date);
    }

    public static String getStringDateByFormat(Context context, Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String getShowTime(Context context, String time) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
        try {
            Date value = simpleDateFormat.parse(time);
            return android.text.format.DateFormat.getTimeFormat(context).format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDateFromTo(String dateStr, String from, String to) {
        DateFormat fromFormat = new SimpleDateFormat(from, Locale.US);
        fromFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat(to, Locale.US);
        toFormat.setLenient(false);
        Date date = null;
        try {
            date = fromFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return toFormat.format(date);
    }

    public static void setImageById(Context context, final ImageView image, int idResource) {
        if (context == null) {
            return;
        }
        Glide.with(context)
                .load(idResource)
                .into(image);
    }

    public static void setImageByUrl(final Context context, final ImageView imageView,
                                     final ProgressBar progressBar, String url) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .override(768, 768)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        progressBar.setVisibility(View.VISIBLE);
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    public static File getOutputMediaFile(Context context) {
        String imageFileName = "avatar";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static File saveBitmap(Context context, Bitmap bmp, String name) {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.close();
            return pictureFile;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }

    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            // noinspection deprecation
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }

    public static RequestBody toRequestBody(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }
}
