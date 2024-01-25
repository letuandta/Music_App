package com.example.musicapp.ui.favorite;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.View;

import com.example.musicapp.BR;
import com.example.musicapp.R;
import com.example.musicapp.databinding.FragmentFavoritesBinding;
import com.example.musicapp.di.component.FragmentComponent;
import com.example.musicapp.ui.base.BaseFragment;

import javax.inject.Inject;

public class FavoritesFragment extends BaseFragment<FragmentFavoritesBinding, FavoriteSongViewModel>{

    @Inject
    FavoriteSongAdapter favoriteSongAdapter;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_favorites;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initAdapter();
        observerDataInViewModel();
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);
    }
    private void initAdapter(){
        mViewDataBinding.rcvFavoriteSong.setAdapter(favoriteSongAdapter);
    };
    private void observerDataInViewModel(){
        mViewModel.getLiveRealmResults().observe(getViewLifecycleOwner(), songs -> {
            Log.e("TAG", "observerDataInViewModel: ");
            favoriteSongAdapter.submitList(songs);
        });
    }

}