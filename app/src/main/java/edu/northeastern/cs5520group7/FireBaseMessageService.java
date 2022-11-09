package edu.northeastern.cs5520group7;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;


public class FireBaseMessageService extends FirebaseMessagingService{
    public static final String TAG = "message";
    private static final String CHANNEL_ID  = "CHANNEL_ID";
    private static final String CHANNEL_NAME  = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION  = "CHANNEL_DESCRIPTION";

    public FireBaseMessageService() {
        super();
        Log.d(TAG, "service running");
    }

    // cited from: https://firebase.google.com/docs/cloud-messaging/android/receive
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "notification received");

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            showNotification(remoteMessage);
        }
    }
    private void showNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);

        }
        else {
            builder = new NotificationCompat.Builder(this);
        }

        Bitmap image = null;

        try{
            URL url = new URL(remoteMessage.getNotification().getImageUrl().toString());
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }

        notification = builder.setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(image)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();


        notificationManager.notify(0,notification);
    }
}
