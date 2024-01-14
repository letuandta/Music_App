package com.example.musicapp.services;

import static com.example.musicapp.common.MusicBundleKey.ACTION_MUSIC;
import static com.example.musicapp.common.MusicBundleKey.ACTION_SEND_DATA_TO_ACTIVITY;
import static com.example.musicapp.common.MusicBundleKey.DURATION;
import static com.example.musicapp.common.MusicBundleKey.IS_PLAYING;
import static com.example.musicapp.common.MusicBundleKey.POSITION;
import static com.example.musicapp.common.MusicBundleKey.SKIP_DURATION;
import static com.example.musicapp.common.MusicBundleKey.SONG_JSON;
import static com.example.musicapp.common.MusicBundleKey.TOTAL;
import static com.example.musicapp.common.MusicBundleKey.TYPE;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_CHANGE_LOOPING;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_CHANGE_SHUFFLE;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_NEXT;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_PREVIOUS;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_SKIP;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_START;
import static com.example.musicapp.common.MusicPlayerActions.ACTION_STOP;
import static com.example.musicapp.common.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.common.MusicPlayerType.RECOMMEND_SONG;

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

import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.models.Song;
import com.example.musicapp.notification.MusicNotification;
import com.example.musicapp.repositories.FavoritesRepository;
import com.example.musicapp.repositories.RecommendsRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class MusicPlayerService extends Service {
    Gson gson = new Gson();
    List<Song> songs;
    int position = 0;
    boolean isPlaying, isLooping, isShuffle;
    String type;
    int actionMusic, skipDuration;
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
            getTypeFromBundle(bundle);
            if(isTypeExist()) {
                getPositionFromBundle(bundle);
                if(isValidPosition())
                    handleTypeData(type);
            }

            getActionFromBundle(bundle);
            getSkipDuration(bundle);
            if (isValidAction()) {
                handleActionMusic(actionMusic, skipDuration);
            }
        }

        return START_NOT_STICKY;
    }

    private void handleTypeData(String type){
        switch (type){
            case RECOMMEND_SONG:
                Log.e("TAG", "onStartCommand: recommend_song");
                songs = RecommendsRepository.readData();
                startMusic();
                break;
            case FAVORITES_SONG:
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
        Intent intent = new Intent(ACTION_SEND_DATA_TO_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_MUSIC, action);
        String songJson = gson.toJson(songs.get(position), Song.class);
        bundle.putString(SONG_JSON, songJson);
        bundle.putInt(POSITION, position);
        bundle.putInt(TOTAL, songs.size());
        bundle.putBoolean(IS_PLAYING, isPlaying);
        bundle.putInt(DURATION, mediaPlayer.getDuration() / 1000);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcastSync(intent);
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
    private boolean isValidAction() {
        return actionMusic > 0;
    }
    private void getSkipDuration(Bundle bundle) {
        skipDuration = bundle.getInt(SKIP_DURATION, -1);
    }
    private void getActionFromBundle(Bundle bundle) {
        actionMusic = bundle.getInt(ACTION_MUSIC, -1);
    }
    private void getTypeFromBundle(Bundle bundle){
        type = bundle.getString(TYPE, "");
    }
    private boolean isTypeExist(){
        return !type.equals("");
    }
    private void getPositionFromBundle(Bundle bundle){
        position = bundle.getInt(POSITION, -1);
    }
    private boolean isValidPosition(){
        return position >= 0;
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
        try {
            if(InternetConnection.isConnected()) {
                if (mediaPlayer != null) {
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
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void playOrPauseMusic(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
        else {
            assert mediaPlayer != null;
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
