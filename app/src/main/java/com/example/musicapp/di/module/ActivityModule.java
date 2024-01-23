package com.example.musicapp.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.ViewModelProviderFactory;
import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.ui.base.BaseActivity;
import com.example.musicapp.ui.list.ListSongViewModel;
import com.example.musicapp.ui.main.MainViewModel;
import com.example.musicapp.ui.player.MusicPlayerViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Provides
    ListSongViewModel provideListSongViewModel(AppDataManager dataManager){
        Supplier<ListSongViewModel> supplier = () -> new ListSongViewModel(dataManager);
        ViewModelProviderFactory<ListSongViewModel> factory = new ViewModelProviderFactory<>(ListSongViewModel.class, supplier);
        return new ViewModelProvider(activity, (ViewModelProvider.Factory) factory).get(ListSongViewModel.class);
    }

    @Provides
    MainViewModel provideMainViewModel(AppDataManager dataManager){
        Supplier<MainViewModel> supplier = () -> new MainViewModel(dataManager);
        ViewModelProviderFactory<MainViewModel> factory = new ViewModelProviderFactory<>(MainViewModel.class, supplier);
        return new ViewModelProvider(activity, (ViewModelProvider.Factory) factory).get(MainViewModel.class);
    }

    @Provides
    MusicPlayerViewModel provideMusicPlayerViewModel(AppDataManager dataManager){
        Supplier<MusicPlayerViewModel> supplier = () -> new MusicPlayerViewModel(dataManager);
        ViewModelProviderFactory<MusicPlayerViewModel> factory = new ViewModelProviderFactory<>(MusicPlayerViewModel.class, supplier);
        return new ViewModelProvider(activity, (ViewModelProvider.Factory) factory).get(MusicPlayerViewModel.class);
    }
}
