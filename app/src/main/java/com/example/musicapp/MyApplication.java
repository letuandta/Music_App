package com.example.musicapp;

import android.app.Application;

import com.example.musicapp.di.component.AppComponent;
import com.example.musicapp.di.component.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    public static Realm musicAppRealm;

    public AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                        .application(this)
                        .build();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("music_app.realm")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(realmConfiguration);
        musicAppRealm = Realm.getDefaultInstance();
    }
}
