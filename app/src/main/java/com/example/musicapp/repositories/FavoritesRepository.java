package com.example.musicapp.repositories;

import com.example.musicapp.models.Artist;
import com.example.musicapp.models.Song;
import com.example.musicapp.MyApplication;

import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

public class FavoritesRepository {

    public void addSong(Song song) {

        Number songId = MyApplication.musicAppRealm.where(Song.class).max("_id");
        Number artistId = MyApplication.musicAppRealm.where(Artist.class).max("_id");
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

        song.setId(nextSongId);
        song.getArtist().setId(nextArtistId);

        MyApplication.musicAppRealm.executeTransaction(r -> {
            MyApplication.musicAppRealm.insert(song);
        });
    }

    public List<Song> readData() {
        RealmResults<Song> realmResult = readSongRealmResult();
        return (List<Song>) MyApplication.musicAppRealm.copyFromRealm(realmResult);
    }

    public RealmResults<Song> readSongRealmResult(){
        return MyApplication.musicAppRealm.where(Song.class)
                .sort("_id", Sort.DESCENDING)
                .limit(10)
                .findAll();
    }
}
