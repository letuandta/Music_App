package com.example.musicapp.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.musicapp.models.Artist;
import com.example.musicapp.models.Song;
import com.example.musicapp.realm.RealmDb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

public class RecommendsRepository {
//    public static final String RECOMMEND_SONG = "recommends_song";
//
//    public static void setRecommendsSongToSharePreferences(Context context, List<Song> list){
//
//        Gson gson = new Gson();
//        SharedPreferences sharedPreferences = context.getSharedPreferences(RECOMMEND_SONG, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Type listSong = new TypeToken<List<Song>>() {}.getType();
//        String songJson = gson.toJson(list, listSong);
//
//        editor.putString(RECOMMEND_SONG, songJson);
//        editor.commit();
//    }

//    public static List<Song> getRecommendsSongFromSharePreferences(Context context){
//        List<Song> list = new ArrayList<>();
//        Gson gson = new Gson();
//        SharedPreferences sharedPreferences = context.getSharedPreferences(RECOMMEND_SONG, Context.MODE_PRIVATE);
//
//        String songJson = sharedPreferences.getString(RECOMMEND_SONG, null);
//        if(songJson != null) {
//            Type listSong = new TypeToken<List<Song>>() {
//            }.getType();
//            list = gson.fromJson(songJson, listSong);
//        }
//
//        return list;
//    }

    public static void addListSong(List<Song> songs){
        songs.forEach(RecommendsRepository::addSong);
    }

    public static void addSong(Song song){

        Number songId = RealmDb.recommendRealm.where(Song.class).max("_id");
        Number artistId = RealmDb.recommendRealm.where(Artist.class).max("_id");
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

        RealmDb.recommendRealm.executeTransaction(r -> {
            RealmDb.recommendRealm.insert(song);
        });
    }

    public static List<Song> readData() {
        RealmResults<Song> realmResult = RealmDb.recommendRealm.where(Song.class).findAll();
        return (List<Song>) RealmDb.recommendRealm.copyFromRealm(realmResult);
    }
}
