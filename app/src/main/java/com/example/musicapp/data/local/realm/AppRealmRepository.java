package com.example.musicapp.data.local.realm;

import com.example.musicapp.MyApplication;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.data.model.local.RecommendSong;
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
        realm.executeTransaction(r -> {
            r.copyToRealmOrUpdate(song);
        });
        realm.close();
    }

    @Override
    public void addRecommendSong(RecommendSong song) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            r.copyToRealmOrUpdate(song);
        });
        realm.close();
    }

    @Override
    public void addListSong(List<Song> list) {
        list.forEach(this::addSong);
    }

    @Override
    public void addListRecommendSong(List<Song> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.delete(RecommendSong.class));
        list.forEach(song -> {
            addRecommendSong(parseToRecommendSong(song));
        });
        realm.close();
    }

    @Override
    public List<Song> getAllFavoriteSong() {
        Realm realm = Realm.getDefaultInstance();
        return  realm.where(Song.class)
                .sort("id", Sort.DESCENDING)
                .findAllAsync();
    }

    @Override
    public RealmResults<Song> getAllFavoriteSongFlowable() {
        return  Realm.getDefaultInstance().where(Song.class)
                .sort("id", Sort.DESCENDING)
                .findAllAsync();
    }

    @Override
    public List<Song> getAllRecommendSong() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RecommendSong> realmResult = realm.where(RecommendSong.class)
                .sort("id", Sort.DESCENDING)
                .findAll();
        return parseToListSong(realmResult);
    }

    @Override
    public List<Song> getListSong(int quantity) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Song.class)
                .sort("id", Sort.DESCENDING)
                .limit(quantity)
                .findAll();
    }

    @Override
    public List<Song> getListRecommendSong(int quantity) {
        Realm realm = Realm.getDefaultInstance();
        List<RecommendSong> realmResult = realm.where(RecommendSong.class)
                .sort("id", Sort.DESCENDING)
                .limit(quantity)
                .findAll();
        return parseToListSong(realmResult);
    }

    @Override
    public List<Song> getListFromKey(String key) {
            Realm realm = Realm.getDefaultInstance();
            List<Song> songs;
            List<RecommendSong> recommendSongs;
            songs = (List<Song>) realm.copyFromRealm(realm.where(Song.class)
                    .contains("title", key)
                    .findAll());
            try {
                if(NetworkUtils.isConnected())
                {
                    recommendSongs = (List<RecommendSong>) realm.copyFromRealm(realm.where(RecommendSong.class)
                            .contains("title", key)
                            .findAll());
                    songs.addAll(parseToListSong(recommendSongs));
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
                .equalTo("id", song.getId())
                .findFirst();
        return result != null && song.getId().equals(result.getId());
    }

    @Override
    public void deleteSong(Song song) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            realm1.where(Song.class).equalTo("id", song.getId()).findAll().deleteAllFromRealm();
        });
        realm.close();
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

    public RecommendSong parseToRecommendSong(Song song) {
        RecommendSong recommendSong = new RecommendSong(song.getTitle(), song.getDuration()
                , song.getPreview(), song.getArtist());
        recommendSong.setId(song.getId());

        return recommendSong;
    }
}
