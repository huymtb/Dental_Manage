package com.prostage.dental_manage.base.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.prostage.dental_manage.DentalManageApp;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.MainActivity;
import com.prostage.dental_manage.core.model.MessageModel;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_ID;


/**
 * Created by congnc on 4/6/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData());
        }
    }

    private void sendNotification(Map<String, String> messageBody) {
        MessageModel model = new MessageModel();

        model.setText(messageBody.get("text"));
        model.setRead(Boolean.parseBoolean(messageBody.get("read")));
        model.setDate(messageBody.get("date"));
        model.setSenderId(messageBody.get("senderId"));
        String id = String.valueOf(Utils.getInt(getApplicationContext(), PREF_ADMIN_ID));
        if (!(model.getSenderId().equals(id)) && !DentalManageApp.isChatActivityOpen()) {
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
}
