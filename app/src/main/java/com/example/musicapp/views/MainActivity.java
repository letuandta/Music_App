package com.example.musicapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.musicapp.R;
import com.example.musicapp.adapter.FavoriteSongAdapter;
import com.example.musicapp.adapter.RecommendSongAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvFavoriteSong = findViewById(R.id.rcv_favorite_song);
        rcvRecommendSong = findViewById(R.id.rcv_recommend_song);

        favoriteSongViewModel = new ViewModelProvider(this).get(FavoriteSongViewModel.class);
        recommendSongViewModel = new ViewModelProvider(this).get(RecommendSongViewModel.class);

        favoriteSongViewModel.getMutableLiveData().observe(this, songs -> {
            favoriteSongAdapter = new FavoriteSongAdapter(songs);
            rcvFavoriteSong.setAdapter(favoriteSongAdapter);
        });

        recommendSongViewModel.getMutableLiveData().observe(this, songs -> {
            recommendSongAdapter = new RecommendSongAdapter(songs);
            rcvRecommendSong.setAdapter(recommendSongAdapter);
        });

    }
}