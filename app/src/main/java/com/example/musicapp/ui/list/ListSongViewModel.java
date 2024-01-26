package com.example.musicapp.ui.list;

import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.RECOMMEND_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.ui.base.BaseViewModel;
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
        if(dataType.equals(FAVORITES_SONG)){
            getCompositeDisposable().add(mDataManager.mRealmRepository
                    .getAllFavoriteSongFlowable()
                    .asFlowable()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mutableLiveData::setValue));
        }

        if(dataType.equals(RECOMMEND_SONG)){
            getCompositeDisposable().add(mDataManager.mRealmRepository
                    .getAllRecommendSongFlowable()
                    .asFlowable()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mutableLiveData::setValue));
        }

        if(dataType.equals(SEARCH_SONG) || dataType.equals(SEARCH_SONG_OFFLINE)){
            getCompositeDisposable().add(mDataManager.mRealmRepository
                    .getListFromKey(key)
                    .asFlowable()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mutableLiveData::setValue));
        }

    }

    public MutableLiveData<List<Song>> getMutableLiveData() {
        return mutableLiveData;
    }
}
