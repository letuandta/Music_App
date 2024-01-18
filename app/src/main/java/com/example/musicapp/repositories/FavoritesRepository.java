package com.example.musicapp.repositories;

import com.example.musicapp.models.Artist;
import com.example.musicapp.models.Song;
import com.example.musicapp.MyApplication;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class FavoritesRepository {

    public void addSong(Song song) {

        MyApplication.musicAppRealm.executeTransaction(r -> {
            MyApplication.musicAppRealm.copyToRealmOrUpdate(song);
        });
    }

    public List<Song> readData() {
        RealmResults<Song> realmResult = readSongRealmResult();
        return (List<Song>) MyApplication.musicAppRealm.copyFromRealm(realmResult);
    }

    public RealmResults<Song> readSongRealmResult(){
        return MyApplication.musicAppRealm.where(Song.class)
                .sort("id", Sort.DESCENDING)
                .limit(10)
                .findAll();
    }

    public boolean exists(String id){
        Song result = MyApplication.musicAppRealm.where(Song.class)
                .equalTo("id", id)
                .findFirst();
        return result != null && id.equals(result.getId());
    }

    public void deleteSong(Song song){
        MyApplication.musicAppRealm.executeTransaction(realm -> {
            realm.where(Song.class).equalTo("id", song.getId()).findAll().deleteAllFromRealm();
        });
    }
}
