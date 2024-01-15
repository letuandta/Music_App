package com.example.musicapp.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDb extends Application { // This class should be named App, or MyApp, not RealmDb

    public static Realm favoriteRealm;
    public static Realm recommendRealm;
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration favoriteConfig = new RealmConfiguration.Builder() // Not necessary to split into 2 Databases
                .name("favorites.realm")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded().build();

        RealmConfiguration recommendConfig = new RealmConfiguration.Builder()
                .name("recommend.realm")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded().build();

        favoriteRealm = Realm.getInstance(favoriteConfig);
        recommendRealm = Realm.getInstance(recommendConfig);
    }
}
