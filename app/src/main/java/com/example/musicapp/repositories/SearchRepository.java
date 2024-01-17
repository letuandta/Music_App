package com.example.musicapp.repositories;

import com.example.musicapp.MyApplication;
import com.example.musicapp.models.Song;
import com.example.musicapp.models.recommend.RecommendSong;

import java.util.ArrayList;
import java.util.List;

public class SearchRepository {
    public List<Song> getListFromKey(String key){
        List<Song> songs;
        List<RecommendSong> recommendSongs;
        songs = (List<Song>) MyApplication.musicAppRealm.copyFromRealm(MyApplication.musicAppRealm.where(Song.class)
                .contains("title", key)
                .findAll());
        recommendSongs = (List<RecommendSong>) MyApplication.musicAppRealm.copyFromRealm(MyApplication.musicAppRealm.where(RecommendSong.class)
                .contains("title", key)
                .findAll());
        songs.addAll(parseToListSong(recommendSongs));
        return songs;
    }

    public List<Song> parseToListSong(List<RecommendSong> recommendSongList) {
        List<Song> songs = new ArrayList<>();
        recommendSongList.forEach(recommendSong -> {
            Song song = new Song(recommendSong.getTitle(), recommendSong.getDuration()
                    , recommendSong.getPreview(), recommendSong.getArtist());
            song.setId(recommendSong.getId());
            songs.add(song);
        });

        return songs;
    }
}