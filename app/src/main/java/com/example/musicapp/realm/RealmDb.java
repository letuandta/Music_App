package com.example.musicapp.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDb extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(configuration);
    }
}
