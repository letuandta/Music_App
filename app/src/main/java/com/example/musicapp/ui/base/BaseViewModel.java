package com.example.musicapp.ui.base;

import androidx.lifecycle.ViewModel;

import com.example.musicapp.data.AppDataManager;

import io.reactivex.disposables.CompositeDisposable;

public class BaseViewModel extends ViewModel {

    protected final AppDataManager mDataManager;

    private final CompositeDisposable compositeDisposable;
    public BaseViewModel(AppDataManager dataManager){
        this.mDataManager = dataManager;
        this.compositeDisposable = new CompositeDisposable();
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public AppDataManager getDataManager() {
        return mDataManager;
    }
}
