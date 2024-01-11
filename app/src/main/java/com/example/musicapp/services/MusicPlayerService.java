package com.example.musicapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.example.musicapp.R;
import com.example.musicapp.models.Song;
import com.example.musicapp.notification.NotificationApplication;
import com.example.musicapp.receivers.NotificationBroadcastReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MusicPlayerService extends Service {

    //music actions
    public static final int ACTION_PLAY_OR_PAUSE = 1;
    public static final int ACTION_START = 2;
    public static final int ACTION_NEXT = 3;
    public static final int ACTION_PREVIOUS = 4;
    public static final int ACTION_STOP = 5;
    public static final int ACTION_CHANGE_SHUFFLE = 6;
    public static final int ACTION_CHANGE_LOOPING = 7;
    public static final int ACTION_SKIP = 8;

    public static final String CHANNEL_ID = "music_channel";

    Gson gson = new Gson();
    List<Song> songs;

    int position = 0;

    boolean isPlaying;

    boolean isLooping;

    MediaPlayer mediaPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String listSongJson = bundle.getString("songs", null);
            if (listSongJson != null) {
                Type listSong = new TypeToken<List<Song>>() {
                }.getType();
                songs = gson.fromJson(bundle.getString("songs"), listSong);
                position = bundle.getInt("position");

                if(mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setLooping(false);
                }
                playMusic();

                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    nextMusic();
                    sendNotification(songs.get(position));
                    sendIntentToActivity(ACTION_START);
                });

                sendNotification(songs.get(position));
                sendIntentToActivity(ACTION_START);
            }

            int actionMusic = bundle.getInt("action_music", -1);
            int skipDuration = bundle.getInt("skip_duration", -1);

            if (actionMusic != -1) {
                handleActionMusic(actionMusic, skipDuration);
            }

        }

        return START_NOT_STICKY;
    }

    private void sendNotification(Song song) {

        final int NOTIFICATION_ID = 412;

        RemoteViews remoteViewsLarge = new RemoteViews(getPackageName(), R.layout.notifycation_large);
        remoteViewsLarge.setTextViewText(R.id.title_song, song.getTitle());
        remoteViewsLarge.setTextViewText(R.id.artist_song, song.getArtist().getName());
        remoteViewsLarge.setOnClickPendingIntent(R.id.icon_play_pause, getPendingIntent(getApplicationContext(), ACTION_PLAY_OR_PAUSE));

        if(isPlaying){
            remoteViewsLarge.setImageViewResource(R.id.icon_play_pause, R.drawable.pause_24);
        }else{
            remoteViewsLarge.setImageViewResource(R.id.icon_play_pause, R.drawable.play_arrow_24);
        }

        remoteViewsLarge.setOnClickPendingIntent(R.id.btn_close, getPendingIntent(getApplicationContext(), ACTION_STOP));
        remoteViewsLarge.setOnClickPendingIntent(R.id.icon_next, getPendingIntent(getApplicationContext(), ACTION_NEXT));
        remoteViewsLarge.setOnClickPendingIntent(R.id.icon_previous, getPendingIntent(getApplicationContext(), ACTION_PREVIOUS));

        createChannelNotification();

        Notification builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomBigContentView(remoteViewsLarge)
                .setSound(null)
                .build();

        startForeground(NOTIFICATION_ID, builder);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationTarget notificationTarget = new NotificationTarget(
                getApplicationContext(),
                R.id.picture_song,
                remoteViewsLarge,
                builder,
                NOTIFICATION_ID
        );

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(song.getArtist().getPicture())
                .override(45, 45)
                .into(notificationTarget);
    }

    private PendingIntent getPendingIntent(Context context, int action){
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt("action_music", action);
        intent.putExtras(bundle);

        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void sendIntentToActivity(int action){
        Intent intent = new Intent("action_send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putInt("action_music", action);
        String songJson = gson.toJson(songs.get(position), Song.class);
        bundle.putString("song_json", songJson);
        bundle.putInt("position", position);
        bundle.putInt("total", songs.size());
        bundle.putBoolean("is_playing", isPlaying);
        bundle.putInt("duration", mediaPlayer.getDuration() / 1000);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(intent);
    }

    private void handleActionMusic(int actionMusic, int skipDuration) {
        switch (actionMusic){
            case ACTION_PLAY_OR_PAUSE:
                playOrPauseMusic();
                sendNotification(songs.get(position));
                sendIntentToActivity(ACTION_PLAY_OR_PAUSE);
                break;
            case ACTION_START:
                sendIntentToActivity(ACTION_START);
                break;
            case ACTION_NEXT:
                nextMusic();
                sendNotification(songs.get(position));
                sendIntentToActivity(ACTION_NEXT);
                break;
            case ACTION_PREVIOUS:
                prevMusic();
                sendNotification(songs.get(position));
                sendIntentToActivity(ACTION_PREVIOUS);
                break;
            case ACTION_STOP:
                sendIntentToActivity(ACTION_STOP);
                stopSelf();
                break;
            case ACTION_CHANGE_SHUFFLE:
                break;
            case ACTION_CHANGE_LOOPING:
                break;
            case ACTION_SKIP:
                skipMusic(skipDuration);
                break;
        }
    }

    private void skipMusic(int skipDuration) {
        if(skipDuration > 0){
            mediaPlayer.seekTo(skipDuration * 1000);
        }
    }

    void playMusic(){
        try {
            Uri uri = Uri.parse(songs.get(position).getPreview());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void playOrPauseMusic(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
        else {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    void nextMusic(){
        if(position < songs.size() - 1){
            position = position + 1;
            Log.e("NEXT method", String.valueOf(position));
            playMusic();
        }else if (position == songs.size() - 1){
            position = 0;
            playMusic();
        }
    }

    void prevMusic(){
        if(position < songs.size() && position > 0){
            position = position - 1;
            playMusic();
        }else if (position == 0){
            position = songs.size() - 1;
            playMusic();
        }
    }

    public void createChannelNotification() {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
