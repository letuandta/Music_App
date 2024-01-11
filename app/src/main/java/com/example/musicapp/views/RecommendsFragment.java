package com.example.musicapp.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.R;
import com.example.musicapp.adapter.RecommendSongAdapter;
import com.example.musicapp.databinding.FragmentRecommendsBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.viewmodels.RecommendSongViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class RecommendsFragment extends Fragment implements
        RecommendSongAdapter.RecommendsSongListener{

    RecyclerView rcvRecommendSong;

    private RecommendSongAdapter recommendSongAdapter;
    private RecommendSongViewModel recommendSongViewModel;

    FragmentRecommendsBinding binding;

    Gson gson = new Gson();


    public static RecommendsFragment newInstance() {
        return new RecommendsFragment();
    }

    private RecommendsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecommendsBinding.inflate(inflater, container, false);
        rcvRecommendSong = binding.rcvRecommendSong;

        recommendSongViewModel = new ViewModelProvider(this).get(RecommendSongViewModel.class);


        recommendSongViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), songs -> {
            recommendSongAdapter = new RecommendSongAdapter(songs, this);
            rcvRecommendSong.setAdapter(recommendSongAdapter);
        });
        return binding.getRoot();
    }

    @Override
    public void onClickRecommendItem(int position) {
        Bundle bundle = new Bundle();
        List<Song> songs = recommendSongViewModel.getMutableLiveData().getValue();
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