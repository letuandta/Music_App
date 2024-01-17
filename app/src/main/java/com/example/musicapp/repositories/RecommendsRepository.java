package com.example.musicapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.Api.SongService;
import com.example.musicapp.models.Artist;
import com.example.musicapp.models.Song;
import com.example.musicapp.models.recommend.Data;
import com.example.musicapp.MyApplication;
import com.example.musicapp.models.recommend.RecommendSong;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendsRepository {

    public void addListSong(List<Song> songs){
        MyApplication.musicAppRealm.executeTransaction(realm -> realm.delete(RecommendSong.class));
        songs.forEach(song -> {
            addSong(parseToRecommendSong(song));
        });
    }

    public void addSong(RecommendSong song){

        Number songId = MyApplication.musicAppRealm.where(RecommendSong.class).max("_id");
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

    public List<Song> readDataFromLocal() {
        RealmResults<RecommendSong> realmResult = MyApplication.musicAppRealm.where(RecommendSong.class).findAll();
        List<RecommendSong> recommendSongs = (List<RecommendSong>) MyApplication.musicAppRealm.copyFromRealm(realmResult);
        return parseToListSong(recommendSongs);
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
        song.setId(song.getId());

        return recommendSong;
    }

    public void loadDataFromApi(MutableLiveData<List<Song>> liveData){
        SongService.callApi.getSong("you")
                .enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Data data = response.body();
                        assert data != null;
                        List<Song> songs = data.getData();
                        addListSong(songs);
                        liveData.setValue(songs);
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {

                    }
                });
    }
}
