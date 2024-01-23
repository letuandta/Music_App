package com.example.musicapp.notification;

import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_NEXT;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_PREVIOUS;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_STOP;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.musicapp.R;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.receivers.MusicNotificationReceiver;

public class MusicNotification{

    public static final String CHANNEL_ID = "music_channel";
    public static final int NOTIFICATION_ID = 412;


    public void createMusicChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "channel for music player", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setSound(null, null);

            NotificationManager manager = context.getSystemService(NotificationManager.class);

            if(manager != null){
                manager.createNotificationChannel(notificationChannel);
            }
        }
    }

    @NonNull
    public Notification musicNotification(Context context, Song song, boolean isPlaying) {

        createMusicChannel(context);

        RemoteViews remoteViewsLarge = new RemoteViews(context.getPackageName(), R.layout.notifycation_large);
        remoteViewsLarge.setTextViewText(R.id.title_song, song.getTitle());
        remoteViewsLarge.setTextViewText(R.id.artist_song, song.getArtist().getName());
        remoteViewsLarge.setOnClickPendingIntent(R.id.icon_play_pause, getPendingIntent(context.getApplicationContext(), ACTION_PLAY_OR_PAUSE));

        if (isPlaying) {
            remoteViewsLarge.setImageViewResource(R.id.icon_play_pause, R.drawable.pause_24);
        } else {
            remoteViewsLarge.setImageViewResource(R.id.icon_play_pause, R.drawable.play_arrow_24);
        }

        remoteViewsLarge.setOnClickPendingIntent(R.id.btn_close, getPendingIntent(context, ACTION_STOP));
        remoteViewsLarge.setOnClickPendingIntent(R.id.icon_next, getPendingIntent(context, ACTION_NEXT));
        remoteViewsLarge.setOnClickPendingIntent(R.id.icon_previous, getPendingIntent(context, ACTION_PREVIOUS));

        Notification builder = new NotificationCompat.Builder(context, MusicNotification.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomBigContentView(remoteViewsLarge)
                .setSound(null)
                .build();

        return builder;
    }

    private PendingIntent getPendingIntent(Context context, int action){
        Intent intent = new Intent(context, MusicNotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action_music", action);
        intent.putExtras(bundle);

        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
