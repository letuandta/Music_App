package com.example.musicapp.ui.favorite;

import static com.example.musicapp.common.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.FAVORITES_SONG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.databinding.FragmentFavoritesBinding;
import com.example.musicapp.ui.player.MusicPlayerActivity;

import java.io.IOException;

public class FavoritesFragment extends Fragment implements FavoriteSongAdapter.FavoritesSongListener{

    FragmentFavoritesBinding binding;
    private FavoriteSongAdapter favoriteSongAdapter;
    private FavoriteSongViewModel favoriteSongViewModel;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflateBinding(inflater, container);
        initViewModel();
        initAdapter();
        observerDataInViewModel();

        return binding.getRoot();
    }

    private void inflateBinding(LayoutInflater inflater, ViewGroup container){
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
    }
    private void initViewModel() {
        favoriteSongViewModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);
    }
    private void initAdapter(){
        favoriteSongAdapter = new FavoriteSongAdapter(this);
        binding.rcvFavoriteSong.setAdapter(favoriteSongAdapter);
    };
    private void observerDataInViewModel(){
        favoriteSongViewModel.getLiveRealmResults().observe(getViewLifecycleOwner(), songs -> {
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