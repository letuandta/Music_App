package com.example.musicapp.data;

import com.example.musicapp.data.local.realm.RealmRepository;
import com.example.musicapp.data.local.user_data.UserDataRepository;
import com.example.musicapp.data.remote.ApiRepository;

import javax.inject.Inject;

public class AppDataManager {
    public RealmRepository mRealmRepository;
    public UserDataRepository mUserDataRepository;
    public ApiRepository mApiRepository;

    @Inject public AppDataManager(RealmRepository mRealmRepository, UserDataRepository mUserDataRepository, ApiRepository mApiRepository) {
        this.mRealmRepository = mRealmRepository;
        this.mUserDataRepository = mUserDataRepository;
        this.mApiRepository = mApiRepository;
    }
}
