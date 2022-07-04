package com.example.sampleadsfcm.fcm;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.sampleadsfcm.activities.MainActivity;
import com.example.sampleadsfcm.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Objects;

public class MyMessaging extends FirebaseMessagingService {
    private static final String TAG = "MyMessaging";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.i(TAG, "onMessageReceived: "+ message);

        // 1 ) check work manager dependency in gradle files .it should be added there
        // 2 ) check application level class and compare with your application level class it should be same
        // 3 ) check in manifest file .. there should be present 3 meta data tags icon,color and channel id


        if (message.getNotification() != null){
            showNotification(message);
        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i(TAG, "onNewToken: "+token);
    }


    private void showNotification(RemoteMessage message){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_IMMUTABLE);

        // in case of min sdk version 21 then use this below code
//        PendingIntent pendingIntent;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            pendingIntent = PendingIntent.getActivity(this,0,intent,
//                    PendingIntent.FLAG_IMMUTABLE);
//        }else {
//            pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
//        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channelId))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(ContextCompat.getColor(this,R.color.white))
                .setContentTitle(Objects.requireNonNull(message.getNotification()).getTitle())
                .setContentText(message.getNotification().getBody())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getNotification().getBody()))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = getString(R.string.app_name);
            String description = message.getNotification().getBody();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channelId),channelName,importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

        }


        notificationManager.notify(1, builder.build());

    }
}
