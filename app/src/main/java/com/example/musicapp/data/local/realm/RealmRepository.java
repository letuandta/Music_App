package com.example.musicapp.data.local.realm;

import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.data.model.local.RecommendSong;

import java.util.List;

import io.realm.RealmResults;

public interface RealmRepository {
    void addSong(Song song);
    void addRecommendSong(RecommendSong song);
    void addListSong(List<Song> list);
    void addListRecommendSong(List<Song> list);
    List<Song> getAllFavoriteSong();
    RealmResults<Song> getAllFavoriteSongFlowable();
    List<Song> getAllRecommendSong();
    List<Song> getListSong(int quantity);
    List<Song> getListRecommendSong(int quantity);
    List<Song> getListFromKey(String key);
    boolean existsSong(Song song);
    void deleteSong(Song song);
}
