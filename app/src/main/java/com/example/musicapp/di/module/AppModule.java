package com.example.musicapp.di.module;

import android.app.Application;
import android.content.Context;

import com.example.musicapp.data.local.realm.AppRealmRepository;
import com.example.musicapp.data.local.realm.RealmRepository;
import com.example.musicapp.data.local.user_data.AppUserDataRepository;
import com.example.musicapp.data.local.user_data.UserDataRepository;
import com.example.musicapp.data.remote.ApiRepository;
import com.example.musicapp.data.remote.AppApiRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class AppModule {

    @Singleton
    @Provides
    Context provideContext(Application application){
        return application;
    }

    @Singleton
    @Provides
    RealmRepository provideRealmRepository(AppRealmRepository appRealmRepository){
        return appRealmRepository;
    }

    @Singleton
    @Provides
    Realm provideRealm(){
        return Realm.getDefaultInstance();
    }

    @Singleton
    @Provides
    UserDataRepository provideUserDataRepository(AppUserDataRepository userDataRepository){
        return userDataRepository;
    }

    @Singleton
    @Provides
    ApiRepository provideApiRepository(AppApiRepository apiRepository){
        return apiRepository;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

}
