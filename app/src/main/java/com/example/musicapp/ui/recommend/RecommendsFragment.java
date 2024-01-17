package com.example.musicapp.ui.recommend;

import static com.example.musicapp.common.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.common.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.common.AppConstants.MusicPlayerType.RECOMMEND_SONG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicapp.common.InternetConnection;
import com.example.musicapp.databinding.FragmentRecommendsBinding;
import com.example.musicapp.ui.player.MusicPlayerActivity;

import java.io.IOException;


public class RecommendsFragment extends Fragment implements
        RecommendSongAdapter.RecommendsSongListener{

    private RecommendSongAdapter recommendSongAdapter;
    private RecommendSongViewModel recommendSongViewModel;

    FragmentRecommendsBinding binding;

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

        return binding.getRoot();
    }

    private void inflateBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentRecommendsBinding.inflate(inflater, container, false);
    }
    private void initViewModel() {
        recommendSongViewModel = new ViewModelProvider(this).get(RecommendSongViewModel.class);
        recommendSongViewModel.setContext(getContext());
        try {
            recommendSongViewModel.initData();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void initAdapter() {
        recommendSongAdapter = new RecommendSongAdapter(this);
        binding.rcvRecommendSong.setAdapter(recommendSongAdapter);
    }
    private void observerDataInViewModel() {
        recommendSongViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), songs -> {
            recommendSongAdapter.submitList(songs);
        });
    }

    @Override
    public void onClickRecommendItem(int position) {
        try {
            if(InternetConnection.isConnected()){
                Bundle bundle = new Bundle();
                bundle.putString(TYPE, RECOMMEND_SONG);
                bundle.putInt(POSITION, position);

                Intent intent = new Intent(getContext(), MusicPlayerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }else {
                Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}