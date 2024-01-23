package com.example.musicapp.di.component;

import com.example.musicapp.di.module.FragmentModule;
import com.example.musicapp.di.scope.FragmentScope;
import com.example.musicapp.ui.favorite.FavoritesFragment;
import com.example.musicapp.ui.recommend.RecommendsFragment;

import dagger.Component;

@FragmentScope
@Component(modules = {FragmentModule.class}, dependencies = {AppComponent.class})
public interface FragmentComponent {

    void inject(FavoritesFragment fragment);
    void inject(RecommendsFragment fragment);
}
