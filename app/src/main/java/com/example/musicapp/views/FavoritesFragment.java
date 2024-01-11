package com.example.musicapp.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.R;
import com.example.musicapp.adapter.FavoriteSongAdapter;
import com.example.musicapp.databinding.FragmentFavoritesBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.viewmodels.FavoriteSongViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements FavoriteSongAdapter.FavoritesSongListener{

    FragmentFavoritesBinding binding;
    RecyclerView rcvFavoriteSong;
    private FavoriteSongAdapter favoriteSongAdapter;
    private FavoriteSongViewModel favoriteSongViewModel;

    Gson gson = new Gson();
    Realm realm = Realm.getDefaultInstance();
    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        rcvFavoriteSong = binding.rcvFavoriteSong;

        favoriteSongViewModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);

        favoriteSongViewModel.getLiveRealmResults().observe(getViewLifecycleOwner(), songs -> {
            favoriteSongAdapter = new FavoriteSongAdapter(songs, this);
            rcvFavoriteSong.setAdapter(favoriteSongAdapter);
        });

        return binding.getRoot();
    }

    @Override
    public void onClickItem(int position) {
        Bundle bundle = new Bundle();
        RealmResults<Song> songsRealm =  favoriteSongViewModel.getLiveRealmResults().getRealmResults();
        List<Song> songs = realm.copyFromRealm(songsRealm);
        Type listSong = new TypeToken<List<Song>>() {}.getType();
        String songJson = gson.toJson(songs, listSong);
        bundle.putString("songs", songJson);
        bundle.putInt("position", position);
        Log.e("TAG", "onTouchEvent: ");

        Intent intent = new Intent(getContext(), MusicPlayerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}