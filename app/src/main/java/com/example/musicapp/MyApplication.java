package com.example.musicapp;

import android.app.Application;

import com.example.musicapp.repositories.FavoritesRepository;
import com.example.musicapp.repositories.RecommendsRepository;
import com.example.musicapp.repositories.SearchRepository;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    public static Realm musicAppRealm;

    public static RecommendsRepository mRecommendsRepository;

    public static FavoritesRepository mFavoritesRepository;

    public static SearchRepository mSearchRepository;
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration favoriteConfig = new RealmConfiguration.Builder()
                .name("music_app.realm")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded().build();


        musicAppRealm = Realm.getInstance(favoriteConfig);

        mFavoritesRepository = new FavoritesRepository();
        mRecommendsRepository = new RecommendsRepository();
        mSearchRepository = new SearchRepository();
    }
}
