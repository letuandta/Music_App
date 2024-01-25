package com.example.musicapp.ui.recommend;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.MyApplication;
import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.data.model.api.Data;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.ui.base.BaseViewModel;
import com.example.musicapp.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecommendSongViewModel extends BaseViewModel {
    private final MutableLiveData<List<Song>> mutableLiveData;

    private final MutableLiveData<String> message;
    public RecommendSongViewModel(AppDataManager appDataManager) {
        super(appDataManager);
        mutableLiveData = new MutableLiveData<>();
        message = new MutableLiveData<>();
    }

    public void initData() throws IOException, InterruptedException {
        getCompositeDisposable().add(mDataManager.mApiRepository
                    .loadSongFromApi()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map(data -> {
                        mDataManager.mRealmRepository.addListRecommendSong(data.getData());
                        return true;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleSuccess, this::handleError));
        mutableLiveData.setValue(mDataManager.mRealmRepository.getListRecommendSong(10));
    }


    private void handleError(Throwable error) {
        message.setValue("Error " + error.getLocalizedMessage());
        Log.e("ERROR", error.getLocalizedMessage());
    }
    private void handleSuccess(boolean b) {
        message.setValue("Get data success! ");
    }

    public MutableLiveData<List<Song>> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }
}
