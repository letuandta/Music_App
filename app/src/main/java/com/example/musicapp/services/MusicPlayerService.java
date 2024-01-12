package com.example.musicapp.services;

import static com.example.musicapp.common.MusicPlayerActions.ACTION_CHANGE_LOOPING;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_CHANGE_SHUFFLE;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_NEXT;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_PREVIOUS;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_SKIP;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_START;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_STOP;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.musicapp.models.Song;
import com.example.musicapp.notification.MusicNotification;
import com.example.musicapp.repositories.FavoritesRepository;
import com.example.musicapp.repositories.SongRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MusicPlayerService extends Service {

    Gson gson = new Gson();
    List<Song> songs;
    int position = 0;
    boolean isPlaying, isLooping, isShuffle;
    MediaPlayer mediaPlayer;

    MusicNotification notification = new MusicNotification();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String type = bundle.getString("type", "");
            if(!type.equals("")) {
                position = bundle.getInt("position");
                handleTypeData(type);
            }

            int actionMusic = bundle.getInt("action_music", -1);
            int skipDuration = bundle.getInt("skip_duration", -1);

            if (actionMusic != -1) {
                handleActionMusic(actionMusic, skipDuration);
            }

        }

        return START_NOT_STICKY;
    }

    private void handleTypeData(String type){
        switch (type){
            case "recommend_song":
                Log.e("TAG", "onStartCommand: recommend_song");
                songs = SongRepository.getRecommendsSongFromSharePreferences(getApplicationContext());
                startMusic();
                break;
            case "favorites_song":
                Log.e("TAG", "onStartCommand: favorites_song");
                songs = FavoritesRepository.readData();
                startMusic();
                break;
        }
    }

    private void sendNotification(Context context, Song song, boolean isPlaying){
        Notification musicNotification = notification.musicNotification(context, song, isPlaying);
        startForeground(MusicNotification.NOTIFICATION_ID, musicNotification);
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
                sendNotification(getApplicationContext(), songs.get(position), isPlaying);
                sendIntentToActivity(ACTION_PLAY_OR_PAUSE);
                break;
            case ACTION_START:
                sendIntentToActivity(ACTION_START);
                break;
            case ACTION_NEXT:
                nextMusic();
                sendNotification(getApplicationContext(), songs.get(position), isPlaying);
                sendIntentToActivity(ACTION_NEXT);
                break;
            case ACTION_PREVIOUS:
                prevMusic();
                sendNotification(getApplicationContext(), songs.get(position), isPlaying);
                sendIntentToActivity(ACTION_PREVIOUS);
                break;
            case ACTION_STOP:
                sendIntentToActivity(ACTION_STOP);
                stopForeground(true);
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
        if(skipDuration > 0 && mediaPlayer != null){
            mediaPlayer.seekTo(skipDuration * 1000);
        }
    }

    void startMusic(){
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
        }
        playMusic();
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            nextMusic();
            sendNotification(getApplicationContext(),songs.get(position), isPlaying);
            sendIntentToActivity(ACTION_START);
        });
        sendNotification(getApplicationContext(), songs.get(position), isPlaying);
        sendIntentToActivity(ACTION_START);
    }

    void playMusic(){
        if(mediaPlayer != null) {
            try {
                Uri uri = Uri.parse(songs.get(position).getPreview());
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void playOrPauseMusic(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
