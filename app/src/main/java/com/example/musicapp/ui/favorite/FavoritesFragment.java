package com.example.musicapp.ui.favorite;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.R;
import com.example.musicapp.databinding.FragmentFavoritesBinding;
import com.example.musicapp.di.component.FragmentComponent;
import com.example.musicapp.ui.base.BaseFragment;
import com.example.musicapp.ui.player.MusicPlayerActivity;

public class FavoritesFragment extends BaseFragment<FragmentFavoritesBinding, FavoriteSongViewModel> implements FavoriteSongAdapter.FavoritesSongListener{

    private FavoriteSongAdapter favoriteSongAdapter;

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
        favoriteSongAdapter = new FavoriteSongAdapter(this);
        mViewDataBinding.rcvFavoriteSong.setAdapter(favoriteSongAdapter);
    };
    private void observerDataInViewModel(){
        mViewModel.getLiveRealmResults().observe(getViewLifecycleOwner(), songs -> {
            Log.e("TAG", "observerDataInViewModel: ");
            favoriteSongAdapter.submitList(songs);
        });
    }

    @Override
    public void onClickItem(int position) {
        Bundle bundle = new Bundle();

        bundle.putString(TYPE, FAVORITES_SONG);
        bundle.putInt(POSITION, position);

        Intent intent = new Intent(getContext(), MusicPlayerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}