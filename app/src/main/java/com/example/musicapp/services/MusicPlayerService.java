package com.example.musicapp.services;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.ACTION_MUSIC;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.ACTION_SEND_DATA_TO_ACTIVITY;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.DURATION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.IS_PLAYING;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.SKIP_DURATION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.SONG_JSON;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TOTAL;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_CHANGE_LOOPING;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_CHANGE_SHUFFLE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_NEXT;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_PREVIOUS;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_SKIP;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_START;
import static com.example.musicapp.utils.AppConstants.MusicPlayerActions.ACTION_STOP;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.RECOMMEND_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicapp.MyApplication;
import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.di.component.DaggerServiceComponent;
import com.example.musicapp.notification.MusicNotification;
import com.example.musicapp.utils.NetworkUtils;


import java.io.File;
import java.io.IOException;
import java.util.List;


import javax.inject.Inject;


public class MusicPlayerService extends Service {
    @Inject
    AppDataManager mDataManager;
    public static boolean isServiceRunning = false;
    List<? extends Song> songs;
    boolean isPlaying;
    String type, keySearch;
    int actionMusic, skipDuration, position = 0;
    MediaPlayer mediaPlayer;

    MusicNotification notification = new MusicNotification();

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerServiceComponent.builder()
                .appComponent(((MyApplication)getApplication()).appComponent)
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            getDataFromBundle(bundle);

            if(isTypeExistAndValidPosition()) {
                handleTypeData(type);
            }

            if (isValidAction()) {
                handleActionMusic(actionMusic, skipDuration);
            }
        }

        MusicPlayerService.isServiceRunning = true;
        return START_NOT_STICKY;
    }

    private void handleTypeData(String type){
        switch (type){
            case RECOMMEND_SONG:
                songs = mDataManager.mRealmRepository.getAllRecommendSong();
                break;
            case FAVORITES_SONG:
                songs = mDataManager.mRealmRepository.getAllFavoriteSong();
                break;
            case SEARCH_SONG:
            case SEARCH_SONG_OFFLINE:
                songs = mDataManager.mRealmRepository.getListFromKey(keySearch);
                break;
        }
        startMusic();
    }

    private void sendNotification(Context context, Song song, boolean isPlaying){
        Notification musicNotification = notification.musicNotification(context, song, isPlaying);
        startForeground(MusicNotification.NOTIFICATION_ID, musicNotification);
    }

    private void handleActionMusic(int actionMusic, int skipDuration) {
        switch (actionMusic){
            case ACTION_PLAY_OR_PAUSE:
                playOrPauseMusic();
                break;
            case ACTION_START:
                sendIntentToActivity(ACTION_START);
                break;
            case ACTION_NEXT:
                nextOrPreviousMusic(ACTION_NEXT);
                break;
            case ACTION_PREVIOUS:
                nextOrPreviousMusic(ACTION_PREVIOUS);
                break;
            case ACTION_STOP:
                sendIntentToActivity(ACTION_STOP);
                stopForeground(true);
                stopSelf();
                break;
            case ACTION_CHANGE_SHUFFLE:
            case ACTION_CHANGE_LOOPING:
            case ACTION_SKIP:
                skipMusic(skipDuration);
                break;
        }
    }
    private boolean isValidAction() {
        return actionMusic > 0;
    }
    private void getDataFromBundle(@NonNull Bundle bundle){
        type = bundle.getString(TYPE, "");
        actionMusic = bundle.getInt(ACTION_MUSIC, -1);
        skipDuration = bundle.getInt(SKIP_DURATION, -1);
        if(isTypeExistAndValidPosition()) {
            position = bundle.getInt(POSITION, -1);
            keySearch = bundle.getString(KEY_SEARCH, "");
        }
    }
    private boolean isTypeExistAndValidPosition(){
        return !type.equals("") && position >= 0;
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
        mediaPlayer.setOnCompletionListener(mediaPlayer -> nextOrPreviousMusic(ACTION_NEXT));
        sendNotification(getApplicationContext(), songs.get(position), isPlaying);
        sendIntentToActivity(ACTION_START);
    }

    void playMusic(){
        try {
            if(mediaPlayer != null) {
                mediaPlayer.reset();
                if (NetworkUtils.isConnected()) {
                    mediaPlayer.setDataSource(this, Uri.parse(songs.get(position).getPreview()));
                }else {
                    Song song = songs.get(position);
                    String fileName = song.getId() + ".mp3";
                    File file = mDataManager.mUserDataRepository.getSong(fileName);
                    if(file.exists())
                        mediaPlayer.setDataSource(Uri.fromFile(file).getPath());
                }
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
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
        else if(mediaPlayer != null){
            mediaPlayer.start();
            isPlaying = true;
        }
        sendNotification(getApplicationContext(), songs.get(position), isPlaying);
        sendIntentToActivity(ACTION_PLAY_OR_PAUSE);
    }

    void nextOrPreviousMusic(int action){
        if(action == ACTION_NEXT) {
            if (position < songs.size() - 1) {
                position = position + 1;
                playMusic();
            } else if (position == songs.size() - 1) {
                position = 0;
                playMusic();
            }
        }else if(action == ACTION_PREVIOUS){
            if(position < songs.size() && position > 0){
                position = position - 1;
                playMusic();
            }else if (position == 0){
                position = songs.size() - 1;
                playMusic();
            }
        }
        sendNotification(getApplicationContext(), songs.get(position), isPlaying);
        sendIntentToActivity(action);
    }

    private void sendIntentToActivity(int action){ // Recommend to use EventBus library
        Intent intent = new Intent(ACTION_SEND_DATA_TO_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_MUSIC, action);
        bundle.putSerializable(SONG_JSON, songs.get(position));
        bundle.putInt(POSITION, position);
        bundle.putInt(TOTAL, songs.size());
        bundle.putBoolean(IS_PLAYING, isPlaying);
        bundle.putInt(DURATION, mediaPlayer.getDuration() / 1000);
        Log.e("DURATION", String.valueOf(mediaPlayer.getDuration() / 1000));
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcastSync(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        MusicPlayerService.isServiceRunning = false;
    }
}
