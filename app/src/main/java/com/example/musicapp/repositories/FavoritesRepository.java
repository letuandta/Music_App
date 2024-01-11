package com.example.musicapp.repositories;

import com.example.musicapp.models.Artist;
import com.example.musicapp.models.Song;

import java.util.List;

import io.realm.Realm;

public class FavoritesRepository {

    private static final Realm realm = Realm.getDefaultInstance();

    public static void addSong(Song song){

        Number songId = realm.where(Song.class).max("_id");
        Number artistId = realm.where(Artist.class).max("_id");
        long nextSongId;
        long nextArtistId;

        if(songId == null){
            nextSongId = 1;
        } else {
            nextSongId = songId.intValue() + 1;
        }

        if(artistId == null){
            nextArtistId = 1;
        } else {
            nextArtistId = artistId.intValue() + 1;
        }

        song.set_id(nextSongId);
        song.getArtist().set_id(nextArtistId);

        realm.executeTransaction(r -> {
            realm.insert(song);
        });
    }

    private List<Song> readData() {
        List<Song> songs = realm.where(Song.class).findAll();
        return songs;
    }
}