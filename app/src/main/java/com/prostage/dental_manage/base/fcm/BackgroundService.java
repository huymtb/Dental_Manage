package com.prostage.dental_manage.base.fcm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.core.model.AdminModel;
import com.prostage.dental_manage.base.MainActivity;
import com.prostage.dental_manage.core.model.MessageModel;
import com.prostage.dental_manage.core.model.UserModel;
import com.prostage.dental_manage.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class BackgroundService extends Service {
    public static final String TAG = "BackgroundService";
    DatabaseReference mDatabaseRef;
    private final IBinder backgroundBind = new BackgroundBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service created");

        FirebaseApp.initializeApp(getApplicationContext());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public class BackgroundBinder extends Binder {
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return backgroundBind;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "In onTaskRemoved");
        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                restartPendingIntent);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendNotification(String text, String date, String senderId) {
        MessageModel model = new MessageModel();
        model.setText(text);
        model.setRead(false);
        model.setDate(date);
        model.setSenderId(senderId);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConstants.NOTI_DATA, model);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("You have a new message")
                .setContentText(model.getText())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            RemoteViews notificationView = new RemoteViews(
                    this.getPackageName(),
                    R.layout.item_notification);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setContent(notificationView);
            notificationView.setImageViewResource(R.id.icon_notification_image, R.mipmap.ic_launcher);
            notificationView.setTextViewText(R.id.title_textview, "You have a new message");
            notificationView.setTextViewText(R.id.content_textview, model.getText());
            String time;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            time = dateFormatter.format(calendar.getTime());
            notificationView.setTextViewText(R.id.time_textview, time);
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }

        int notificationId = (int) (System.currentTimeMillis() % 10000);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, builder.build());
    }
}
