package com.example.musicapp.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.musicapp.models.Song;
import com.example.musicapp.realm.LiveRealmResults;

import io.realm.Realm;
import io.realm.Sort;

public class FavoriteSongViewModel extends ViewModel {
    private final LiveRealmResults<Song> liveRealmResults;

    public FavoriteSongViewModel() {
        Realm realm = Realm.getDefaultInstance();
        liveRealmResults = new LiveRealmResults<>(realm.where(Song.class)
                .sort("_id", Sort.DESCENDING)
                .limit(7)
                .findAll());
    }

    public LiveRealmResults<Song> getLiveRealmResults() {
        return liveRealmResults;
    }
}
