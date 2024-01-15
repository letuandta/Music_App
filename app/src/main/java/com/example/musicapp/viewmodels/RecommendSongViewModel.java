package com.example.musicapp.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Api.SongService;
import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.models.Data;
import com.example.musicapp.models.Song;
import com.example.musicapp.repositories.RecommendsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendSongViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mutableLiveData;

    private Context context;
    public void setContext(Context context) {
        this.context = context;
    }

    public RecommendSongViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public void initData() {
        assert context != null;
                SongService.callApi.getSong("you") // Must be handled by repository
                        .enqueue(new Callback<Data>() {
                            @Override
                            public void onResponse(Call<Data> call, Response<Data> response) {
                                Data data = response.body();
                                assert data != null;
                                List<Song> songs = data.getData();
                                RecommendsRepository.addListSong(songs);
                                mutableLiveData.setValue(songs);
                            }

                            @Override
                            public void onFailure(Call<Data> call, Throwable t) {

                            }
                        });

    }

    public MutableLiveData<List<Song>> getMutableLiveData() {
        return mutableLiveData;
    }

}
