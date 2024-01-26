package com.example.musicapp.ui.recommend;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.SONG_ID;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.RECOMMEND_SONG;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableField;

import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.ui.player.MusicPlayerActivity;
import com.example.musicapp.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ItemRecommendViewModel {
    private final ObservableField<Song> song = new ObservableField<>();
    private int position;

    AppDataManager mDataManager;
    public ItemRecommendViewModel(AppDataManager appDataManager, Song song, int position) {
        this.song.set(song);
        this.position = position;
        this.mDataManager = appDataManager;
    }

    public ObservableField<Song> getSong() {
        return song;
    }

    public void onClickItem(View view){
        try {
            if(NetworkUtils.isConnected()){
                Bundle bundle = new Bundle();
                bundle.putString(TYPE, RECOMMEND_SONG);
                bundle.putString(SONG_ID, Objects.requireNonNull(song.get()).getId());

                Intent intent = new Intent(view.getContext(), MusicPlayerActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

            }else {
                Toast.makeText(view.getContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onClickAddSong(View view){
        boolean isFavorite = mDataManager.mRealmRepository.existsSong(song.get());
        try {
            if(!isFavorite) {
                if (NetworkUtils.isConnected()) {
                    try {
                        mDataManager.mRealmRepository.addSong(song.get());
                        String fileName = Objects.requireNonNull(song.get()).getId() + ".mp3";
                        File file = mDataManager.mUserDataRepository.getSong(fileName);
                        if (!file.exists())
                            mDataManager.mUserDataRepository.downloadSong(song.get());
                        Toast.makeText(view.getContext(), "Add song into favorites list success", Toast.LENGTH_LONG).show();
                        view.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        Log.e("FAVORITES LIST", "ERROR: " + e.getMessage());
                    }
                }else Toast.makeText(view.getContext(), "No internet", Toast.LENGTH_LONG).show();
            }else Toast.makeText(view.getContext(), "This song already exists", Toast.LENGTH_LONG).show();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
