package com.example.musicapp.repositories;

import com.example.musicapp.models.Artist;
import com.example.musicapp.models.Song;
import com.example.musicapp.realm.RealmDb;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class FavoritesRepository {

    public static void addSong(Song song) {

        Number songId = RealmDb.favoriteRealm.where(Song.class).max("_id");
        Number artistId = RealmDb.favoriteRealm.where(Artist.class).max("_id");
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

        RealmDb.favoriteRealm.executeTransaction(r -> {
            RealmDb.favoriteRealm.insert(song);
        });
    }

    public static List<Song> readData() {
        RealmResults<Song> realmResult = RealmDb.favoriteRealm.where(Song.class)
                .sort("_id", Sort.DESCENDING)
                .limit(10)
                .findAll();
        return (List<Song>) RealmDb.favoriteRealm.copyFromRealm(realmResult);
    }
}
