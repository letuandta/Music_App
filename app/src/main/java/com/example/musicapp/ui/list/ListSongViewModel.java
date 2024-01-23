package com.example.musicapp.ui.list;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.MyApplication;
import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.ui.base.BaseViewModel;
import com.example.musicapp.utils.AppConstants;
import com.example.musicapp.data.model.local.Song;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListSongViewModel extends BaseViewModel {

    private final MutableLiveData<List<Song>> mutableLiveData;

    public ListSongViewModel(AppDataManager dataManager) {
        super(dataManager);
        mutableLiveData = new MutableLiveData<>();
    }

    public void initData(String dataType, String key){
        if(dataType.equals(AppConstants.MusicPlayerType.FAVORITES_SONG)){
            getCompositeDisposable().add(mDataManager.mRealmRepository
                    .getAllFavoriteSongFlowable()
                    .asFlowable()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mutableLiveData::setValue));
        }

        if(dataType.equals(AppConstants.MusicPlayerType.RECOMMEND_SONG)){
            mutableLiveData.setValue(mDataManager.mRealmRepository.getAllRecommendSong());
        }

        if(dataType.equals(AppConstants.MusicPlayerType.SEARCH_SONG) || dataType.equals(AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE)){
            mutableLiveData.setValue(mDataManager.mRealmRepository.getListFromKey(key));
        }

    }

    public MutableLiveData<List<Song>> getMutableLiveData() {
        return mutableLiveData;
    }
}
