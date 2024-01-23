package com.example.musicapp.data.remote;

import com.example.musicapp.data.model.api.Data;
import com.example.musicapp.data.model.local.Song;

import io.reactivex.Observable;

public interface ApiRepository {
    Observable<Data<Song>> loadSongFromApi();
}
