package com.example.musicapp.data.remote;

import com.example.musicapp.data.local.realm.RealmRepository;
import com.example.musicapp.data.model.api.Data;
import com.example.musicapp.data.model.local.Song;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AppApiRepository implements ApiRepository{

    RealmRepository realmRepository;
    @Inject public AppApiRepository(RealmRepository realmRepository) {
        this.realmRepository = realmRepository;
    }

    @Override
    public Observable<Data<Song>> loadSongFromApi() {
       return SongService.callApi.getSong("you");
    }
}
