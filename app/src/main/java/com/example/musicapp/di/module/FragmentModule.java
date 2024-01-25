package com.example.musicapp.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicapp.ViewModelProviderFactory;
import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.ui.base.BaseFragment;
import com.example.musicapp.ui.favorite.FavoriteSongAdapter;
import com.example.musicapp.ui.favorite.FavoriteSongViewModel;
import com.example.musicapp.ui.favorite.ItemFavoriteViewModel;
import com.example.musicapp.ui.list.ListSongViewModel;
import com.example.musicapp.ui.recommend.RecommendSongAdapter;
import com.example.musicapp.ui.recommend.RecommendSongViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {
    private BaseFragment<?, ?> fragment;

    public FragmentModule(BaseFragment<?, ?> fragment) {
        this.fragment = fragment;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    FavoriteSongViewModel provideFavoriteSongViewModel(AppDataManager dataManager){
        Supplier<FavoriteSongViewModel> supplier = () -> new FavoriteSongViewModel(dataManager);
        ViewModelProviderFactory<FavoriteSongViewModel> factory = new ViewModelProviderFactory<>(FavoriteSongViewModel.class, supplier);
        return new ViewModelProvider(fragment, (ViewModelProvider.Factory) factory).get(FavoriteSongViewModel.class);
    }

    @Provides
    RecommendSongViewModel provideRecommendSongViewModel(AppDataManager dataManager){
        Supplier<RecommendSongViewModel> supplier = () -> new RecommendSongViewModel(dataManager);
        ViewModelProviderFactory<RecommendSongViewModel> factory = new ViewModelProviderFactory<>(RecommendSongViewModel.class, supplier);
        return new ViewModelProvider(fragment, (ViewModelProvider.Factory) factory).get(RecommendSongViewModel.class);
    }

    @Provides
    FavoriteSongAdapter provideFavoriteAdapter(){
        return new FavoriteSongAdapter();
    }

    @Provides
    RecommendSongAdapter provideRecommendAdapter(AppDataManager appDataManager){
        return new RecommendSongAdapter(appDataManager);
    }

}
