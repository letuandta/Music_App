package com.example.musicapp.ui.favorite;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.ui.base.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FavoriteSongViewModel extends BaseViewModel {

    private final MutableLiveData<List<Song>> liveRealmResults;

    public FavoriteSongViewModel(AppDataManager appDataManager) {
        super(appDataManager);
        liveRealmResults = new MutableLiveData<>();
        init();
    }

    private void init(){
        getCompositeDisposable().add(mDataManager.mRealmRepository
                .getAllFavoriteSongFlowable()
                .asFlowable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(liveRealmResults::setValue));
    }

    public MutableLiveData<List<Song>> getLiveRealmResults() {
        return liveRealmResults;
    }
}
