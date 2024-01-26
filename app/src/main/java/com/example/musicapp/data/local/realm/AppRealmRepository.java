package com.example.musicapp.data.local.realm;

import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.RECOMMEND_SONG;

import android.util.Log;

import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

@Singleton
public class AppRealmRepository implements RealmRepository{


    @Inject
    public AppRealmRepository() {
    }

    @Override
    public void addSong(Song song) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(r -> {
            Song song1 = r.where(Song.class)
                    .equalTo("id", song.getId())
                    .findFirst();
            if (song1 != null) {
                song1.setType(FAVORITES_SONG);
            }
        });
        realm.close();
    }

    @Override
    public void addRecommendSong(Song song) {
        song.setType(RECOMMEND_SONG);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> r.copyToRealmOrUpdate(song));
        realm.close();
    }

    @Override
    public void addListSong(List<Song> list) {
        list.forEach(this::addSong);
    }

    @Override
    public void addListRecommendSong(List<Song> list) {
        Realm realm = Realm.getDefaultInstance();
        List<Song> listFake = new ArrayList<>(list);
        for (Song song: listFake) {
            if (existsSong(song)){
                list.remove(song);
                Log.e("FAVORITES LIST", "remove");
            } else {
                Log.e("FAVORITES LIST", "change type");
                song.setType(RECOMMEND_SONG);
            }
        }
        Log.e("FAVORITES LIST", String.valueOf(list.size()));
        if(!list.isEmpty()){
            realm.executeTransactionAsync(realm1 -> {
                realm1.where(Song.class).equalTo("type", RECOMMEND_SONG).findAll().deleteAllFromRealm();
                realm1.copyToRealmOrUpdate(list);
            });
        }
        realm.close();
    }

    @Override
    public List<Song> getAllFavoriteSong() {
        Realm realm = Realm.getDefaultInstance();
        return  realm.where(Song.class)
                .equalTo("type", FAVORITES_SONG)
                .sort("id", Sort.DESCENDING)
                .findAll();
    }

    @Override
    public RealmResults<Song> getAllFavoriteSongFlowable() {
        return  Realm.getDefaultInstance().where(Song.class)
                .equalTo("type", FAVORITES_SONG)
                .sort("id", Sort.DESCENDING)
                .findAllAsync();
    }

    @Override
    public List<Song> getAllRecommendSong() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Song.class)
                .equalTo("type", RECOMMEND_SONG)
                .sort("id", Sort.DESCENDING)
                .findAll();
    }

    @Override
    public RealmResults<Song> getAllRecommendSongFlowable() {
        return  Realm.getDefaultInstance().where(Song.class)
                .equalTo("type", RECOMMEND_SONG)
                .sort("id", Sort.DESCENDING)
                .findAll();
    }

    @Override
    public RealmResults<Song> getListFavoriteSong(int quantity) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Song.class)
                .equalTo("type", FAVORITES_SONG)
                .sort("id", Sort.DESCENDING)
                .limit(quantity)
                .findAllAsync();
    }

    @Override
    public RealmResults<Song> getListRecommendSong(int quantity) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Song.class)
                .equalTo("type", RECOMMEND_SONG)
                .sort("id", Sort.DESCENDING)
                .limit(quantity)
                .findAllAsync();
    }

    @Override
    public RealmResults<Song> getListFromKey(String key) {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Song> songs;
            try {
                if(NetworkUtils.isConnected())
                {
                    songs = realm.where(Song.class)
                                    .contains("title", key)
                                    .findAll();
                }
                else {
                    songs = realm.where(Song.class)
                            .equalTo("type", FAVORITES_SONG)
                            .contains("title", key)
                            .findAll();
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
            return songs;
    }

    @Override
    public boolean existsSong(Song song) {
        Realm realm = Realm.getDefaultInstance();
        Song result = realm.where(Song.class)
                .equalTo("type", FAVORITES_SONG)
                .equalTo("id", song.getId())
                .findFirst();
        return result != null && song.getId().equals(result.getId());
    }

    @Override
    public void deleteSong(Song song) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.where(Song.class).equalTo("id", song.getId()).findAll().deleteAllFromRealm());
        realm.close();
    }
}
