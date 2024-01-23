package com.example.musicapp.di.component;

import com.example.musicapp.di.module.ActivityModule;
import com.example.musicapp.di.scope.ActivityScope;
import com.example.musicapp.ui.list.ListSongActivity;
import com.example.musicapp.ui.main.MainActivity;
import com.example.musicapp.ui.player.MusicPlayerActivity;

import dagger.Component;


@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(ListSongActivity activity);
    void inject(MainActivity activity);

    void inject(MusicPlayerActivity activity);
}
