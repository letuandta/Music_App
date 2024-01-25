package com.example.musicapp.ui.recommend;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.musicapp.BR;
import com.example.musicapp.R;
import com.example.musicapp.databinding.FragmentRecommendsBinding;
import com.example.musicapp.di.component.FragmentComponent;
import com.example.musicapp.ui.base.BaseFragment;

import java.io.IOException;

import javax.inject.Inject;


public class RecommendsFragment extends BaseFragment<FragmentRecommendsBinding, RecommendSongViewModel>{

    @Inject
    RecommendSongAdapter recommendSongAdapter;

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
        mViewDataBinding.rcvRecommendSong.setAdapter(recommendSongAdapter);
    }
    private void observerDataInViewModel() {
        mViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), songs -> {
            recommendSongAdapter.submitList(songs);
        });
        mViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}