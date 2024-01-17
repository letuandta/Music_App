package com.example.musicapp.ui.favorite;

import androidx.lifecycle.ViewModel;

import com.example.musicapp.models.Song;
import com.example.musicapp.realm.LiveRealmResults;
import com.example.musicapp.MyApplication;

import io.realm.Sort;

public class FavoriteSongViewModel extends ViewModel {
    private final LiveRealmResults<Song> liveRealmResults;

    public FavoriteSongViewModel() {
        liveRealmResults = new LiveRealmResults<>(
                MyApplication.mFavoritesRepository.readSongRealmResult()
        );
    }

    public LiveRealmResults<Song> getLiveRealmResults() {
        return liveRealmResults;
    }
}
