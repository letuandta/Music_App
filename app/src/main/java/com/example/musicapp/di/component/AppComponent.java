package com.example.musicapp.di.component;

import android.app.Application;

import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.di.module.AppModule;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    AppDataManager getDataManger();

    Gson getGson();
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}
