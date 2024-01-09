package com.example.musicapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.musicapp.R;
import com.example.musicapp.adapter.FavoriteSongAdapter;
import com.example.musicapp.adapter.RecommendSongAdapter;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.viewmodels.FavoriteSongViewModel;
import com.example.musicapp.viewmodels.RecommendSongViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcvFavoriteSong;
    RecyclerView rcvRecommendSong;

    private FavoriteSongAdapter favoriteSongAdapter;
    private FavoriteSongViewModel favoriteSongViewModel;

    private RecommendSongAdapter recommendSongAdapter;
    private RecommendSongViewModel recommendSongViewModel;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());

        rcvFavoriteSong = binding.rcvFavoriteSong;
        rcvRecommendSong = binding.rcvRecommendSong;

        favoriteSongViewModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);
        recommendSongViewModel = new ViewModelProvider(this).get(RecommendSongViewModel.class);

        favoriteSongViewModel.getLiveRealmResults().observe(this, songs -> {
            favoriteSongAdapter = new FavoriteSongAdapter(songs);
            rcvFavoriteSong.setAdapter(favoriteSongAdapter);
        });

        recommendSongViewModel.getMutableLiveData().observe(this, songs -> {
            recommendSongAdapter = new RecommendSongAdapter(songs);
            rcvRecommendSong.setAdapter(recommendSongAdapter);
        });

    }
}