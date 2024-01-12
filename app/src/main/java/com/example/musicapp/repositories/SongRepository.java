package com.example.musicapp.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.musicapp.Api.SongService;
import com.example.musicapp.models.Data;
import com.example.musicapp.models.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {
    public static final String RECOMMEND_SONG = "recommends_song";

    public static void setRecommendsSongToSharePreferences(Context context, List<Song> list){

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECOMMEND_SONG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Type listSong = new TypeToken<List<Song>>() {}.getType();
        String songJson = gson.toJson(list, listSong);

        editor.putString(RECOMMEND_SONG, songJson);
        editor.commit();
    }

    public static List<Song> getRecommendsSongFromSharePreferences(Context context){
        List<Song> list = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECOMMEND_SONG, Context.MODE_PRIVATE);

        String songJson = sharedPreferences.getString(RECOMMEND_SONG, null);
        if(songJson != null) {
            Type listSong = new TypeToken<List<Song>>() {
            }.getType();
            list = gson.fromJson(songJson, listSong);
        }

        return list;
    }
}
