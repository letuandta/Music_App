package com.example.musicapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.data.remote.SongService;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.data.model.api.Data;
import com.example.musicapp.MyApplication;
import com.example.musicapp.data.model.local.RecommendSong;

import java.util.ArrayList;
import java.util.List;

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
        MyApplication.musicAppRealm.executeTransaction(r -> {
            MyApplication.musicAppRealm.copyToRealmOrUpdate(song);
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
        recommendSong.setId(song.getId());

        return recommendSong;
    }

//   // public void loadDataFromApi(MutableLiveData<List<Song>> liveData){
//        SongService.callApi.getSong("you")
//                .enqueue(new Callback<Data<Song>>() {
//                    @Override
//                    public void onResponse(Call<Data<Song>> call, Response<Data<Song>> response) {
//                        Data<Song> data = response.body();
//                        assert data != null;
//                        List<Song> songs = data.getData();
//                        addListSong(songs);
//                        liveData.setValue(songs);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Data<Song>> call, Throwable t) {
//
//                    }
//                });
//    }
}
