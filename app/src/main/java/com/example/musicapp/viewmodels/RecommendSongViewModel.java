package com.example.musicapp.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Api.SongService;
import com.example.musicapp.models.Data;
import com.example.musicapp.models.Song;
import com.example.musicapp.repositories.SongRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendSongViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mutableLiveData;

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    public RecommendSongViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public void initData() {
        SongService.callApi.getSong("you")
                .enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Data data = response.body();
                        List<Song> songs = data.getData();
                        mutableLiveData.setValue(songs);
                        SongRepository.setRecommendsSongToSharePreferences(context, songs);
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
