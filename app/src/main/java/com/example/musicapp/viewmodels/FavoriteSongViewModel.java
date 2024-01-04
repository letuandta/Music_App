package com.example.musicapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.fakedata.FakeData;
import com.example.musicapp.models.Song;

import java.util.List;

public class FavoriteSongViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mutableLiveData;

    public FavoriteSongViewModel() {
        mutableLiveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        mutableLiveData.setValue(FakeData.song);
    }

    public MutableLiveData<List<Song>> getMutableLiveData() {
        return mutableLiveData;
    }
}
