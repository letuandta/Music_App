package com.example.musicapp.ui.list;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.MyApplication;
import com.example.musicapp.common.AppConstants;
import com.example.musicapp.models.Song;

import java.util.List;

public class ListSongViewModel extends ViewModel {

    private final MutableLiveData<List<Song>> mutableLiveData;

    public ListSongViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public void initData(String dataType, String key){
        if(dataType.equals(AppConstants.MusicPlayerType.FAVORITES_SONG)){
            mutableLiveData.setValue(MyApplication.mFavoritesRepository.readSongRealmResult());
        }

        if(dataType.equals(AppConstants.MusicPlayerType.RECOMMEND_SONG)){
            mutableLiveData.setValue(MyApplication.mRecommendsRepository.readDataFromLocal());
        }

        if(dataType.equals(AppConstants.MusicPlayerType.SEARCH_SONG) || dataType.equals(AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE)){
            mutableLiveData.setValue(MyApplication.mSearchRepository.getListFromKey(key));
        }

    }

    public MutableLiveData<List<Song>> getMutableLiveData() {
        return mutableLiveData;
    }
}
