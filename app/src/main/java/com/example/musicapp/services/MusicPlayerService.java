package com.example.musicapp.services;

import static com.example.musicapp.common.AppConstants.MusicBundleKey.ACTION_MUSIC;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.ACTION_SEND_DATA_TO_ACTIVITY;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.DURATION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.IS_PLAYING;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.SKIP_DURATION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.SONG_JSON;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.TOTAL;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_CHANGE_LOOPING;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_CHANGE_SHUFFLE;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_NEXT;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_PLAY_OR_PAUSE;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_PREVIOUS;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_SKIP;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_START;
import static com.example.musicapp.common.AppConstants.MusicPlayerActions.ACTION_STOP;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.RECOMMEND_SONG;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.SEARCH_SONG;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

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
import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.models.Song;
import com.example.musicapp.notification.MusicNotification;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MusicPlayerService extends Service {

    public static boolean isServiceRunning = false;
    Gson gson = new Gson();
    List<? extends Song> songs;
    int position = 0;
    boolean isPlaying;
    String type, keySearch;
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
            getDataFromBundle(bundle);
            if(isTypeExist()) {
                if(isValidPosition())
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
                songs = MyApplication.mRecommendsRepository.readDataFromLocal();
                break;
            case FAVORITES_SONG:
                songs = MyApplication.mFavoritesRepository.readData();
                break;
            case SEARCH_SONG:
            case SEARCH_SONG_OFFLINE:
                songs = MyApplication.mSearchRepository.getListFromKey(keySearch);
                break;
        }
        startMusic();
    }

    private void sendNotification(Context context, Song song, boolean isPlaying){
        Notification musicNotification = notification.musicNotification(context, song, isPlaying);
        startForeground(MusicNotification.NOTIFICATION_ID, musicNotification);
    }

    private void sendIntentToActivity(int action){ // Recommend to use EventBus library
        Intent intent = new Intent(ACTION_SEND_DATA_TO_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_MUSIC, action);
        String songJson = gson.toJson(songs.get(position), Song.class);
        bundle.putString(SONG_JSON, songJson);
        bundle.putInt(POSITION, position);
        bundle.putInt(TOTAL, songs.size());
        bundle.putBoolean(IS_PLAYING, isPlaying);
        bundle.putInt(DURATION, mediaPlayer.getDuration() / 1000);
        Log.e("DUARATION", String.valueOf(mediaPlayer.getDuration() / 1000));
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcastSync(intent);
    }

    private void handleActionMusic(int actionMusic, int skipDuration) { // Can be optimized to reduce duplicated code
        switch (actionMusic){
            case ACTION_PLAY_OR_PAUSE:
                playOrPauseMusic();
                break;
            case ACTION_START:
                sendIntentToActivity(ACTION_START);
                break;
            case ACTION_NEXT:
                nextMusic();
                break;
            case ACTION_PREVIOUS:
                prevMusic();
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
        if(isTypeExist()) {
            position = bundle.getInt(POSITION, -1);
            keySearch = bundle.getString(KEY_SEARCH, "");
        }
    }
    private boolean isTypeExist(){
        return !type.equals("");
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
        });
        sendNotification(getApplicationContext(), songs.get(position), isPlaying);
        sendIntentToActivity(ACTION_START);
    }

    void playMusic(){
        try {
            if(mediaPlayer != null) {
                mediaPlayer.reset();
                if (InternetConnection.isConnected()) {
                    mediaPlayer.setDataSource(this, Uri.parse(songs.get(position).getPreview()));
                }else {
                    Song song = songs.get(position);
                    String fileName = song.getId() + ".mp3";
                    File file = MyApplication.mOfflineRepository.getSong(getApplicationContext(),fileName);
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

    void nextMusic(){
        if(position < songs.size() - 1){
            position = position + 1;
            playMusic();
        }else if (position == songs.size() - 1){
            position = 0;
            playMusic();
        }
        sendNotification(getApplicationContext(), songs.get(position), isPlaying);
        sendIntentToActivity(ACTION_NEXT);
    }

    void prevMusic(){
        if(position < songs.size() && position > 0){
            position = position - 1;
            playMusic();
        }else if (position == 0){
            position = songs.size() - 1;
            playMusic();
        }
        sendNotification(getApplicationContext(), songs.get(position), isPlaying);
        sendIntentToActivity(ACTION_PREVIOUS);
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
