package com.example.musicapp.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationApplication extends Application {

    public static final String CHANNEL_ID = "music_channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createChannelNotification();
    }

    private void createChannelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "channel for music player", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setSound(null, null);

            NotificationManager manager = getSystemService(NotificationManager.class);

            if(manager != null){
                manager.createNotificationChannel(notificationChannel);
            }
        }
    }
}
