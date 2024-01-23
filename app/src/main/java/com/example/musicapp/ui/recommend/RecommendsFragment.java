package com.example.musicapp.ui.recommend;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.RECOMMEND_SONG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.databinding.FragmentRecommendsBinding;
import com.example.musicapp.di.component.FragmentComponent;
import com.example.musicapp.ui.base.BaseFragment;
import com.example.musicapp.ui.player.MusicPlayerActivity;
import com.example.musicapp.utils.NetworkUtils;

import java.io.IOException;


public class RecommendsFragment extends BaseFragment<FragmentRecommendsBinding, RecommendSongViewModel> implements
        RecommendSongAdapter.RecommendsSongListener{

    private RecommendSongAdapter recommendSongAdapter;

    public static RecommendsFragment newInstance() {
        return new RecommendsFragment();
    }

    private RecommendsFragment(){}

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recommends;
    }

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

        return mViewDataBinding.getRoot();
    }

    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void inflateBinding(LayoutInflater inflater, ViewGroup container) {
        mViewDataBinding = FragmentRecommendsBinding.inflate(inflater, container, false);
    }
    private void initViewModel() {
        try {
            mViewModel.initData();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void initAdapter() {
        recommendSongAdapter = new RecommendSongAdapter(this);
        mViewDataBinding.rcvRecommendSong.setAdapter(recommendSongAdapter);
    }
    private void observerDataInViewModel() {
        mViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), songs -> {
            recommendSongAdapter.submitList(songs);
        });
    }

    @Override
    public void onClickRecommendItem(int position) {
        try {
            if(NetworkUtils.isConnected()){
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