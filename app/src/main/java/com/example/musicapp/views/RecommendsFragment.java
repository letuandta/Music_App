package com.example.musicapp.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflateBinding(inflater, container);
        initViewModel();
        initAdapter();
        observerDataInViewModel();
        setAdapterForRecycleView();

        return binding.getRoot();
    }

    private void inflateBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentRecommendsBinding.inflate(inflater, container, false);
    }
    private void initViewModel() {
        recommendSongViewModel = new ViewModelProvider(this).get(RecommendSongViewModel.class);
        recommendSongViewModel.setContext(getContext());
        recommendSongViewModel.initData();
    }
    private void initAdapter() {
        recommendSongAdapter = new RecommendSongAdapter(this);
    }
    private void observerDataInViewModel() {
        recommendSongViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), songs -> {
            recommendSongAdapter.submitList(songs);
        });
    }
    private void setAdapterForRecycleView() {
        binding.rcvRecommendSong.setAdapter(recommendSongAdapter);
    }

    @Override
    public void onClickRecommendItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "recommend_song");
        bundle.putInt("position", position);

        Intent intent = new Intent(getContext(), MusicPlayerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}