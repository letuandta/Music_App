package com.example.musicapp.data.local.realm;

import com.example.musicapp.data.model.local.Song;

import java.util.List;

import io.realm.RealmResults;

public interface RealmRepository {
    void addSong(Song song);
    void addRecommendSong(Song song);
    void addListSong(List<Song> list);
    void addListRecommendSong(List<Song> list);
    List<Song> getAllFavoriteSong();
    RealmResults<Song> getAllFavoriteSongFlowable();
    List<Song> getAllRecommendSong();
    RealmResults<Song> getAllRecommendSongFlowable();
    RealmResults<Song> getListFavoriteSong(int quantity);
    RealmResults<Song> getListRecommendSong(int quantity);
    RealmResults<Song> getListFromKey(String key);
    boolean existsSong(Song song);
    void deleteSong(Song song);
}
