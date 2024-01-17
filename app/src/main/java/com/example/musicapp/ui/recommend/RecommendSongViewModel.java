package com.example.musicapp.ui.recommend;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.MyApplication;
import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.models.Song;
import com.example.musicapp.repositories.RecommendsRepository;

import java.io.IOException;
import java.util.List;

public class RecommendSongViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mutableLiveData;

    private Context context;

    public RecommendSongViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public void initData() throws IOException, InterruptedException {
        if(context != null && InternetConnection.isConnected()){
            MyApplication.mRecommendsRepository.loadDataFromApi(mutableLiveData);
        }else{
            mutableLiveData.setValue(MyApplication.mRecommendsRepository.readDataFromLocal());
        }
    }

    public MutableLiveData<List<Song>> getMutableLiveData() {
        return mutableLiveData;
    }
    public void setContext(Context context) {
        this.context = context;
    }
}
