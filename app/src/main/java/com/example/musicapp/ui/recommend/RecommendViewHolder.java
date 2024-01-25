package com.example.musicapp.ui.recommend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.MyApplication;
import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.databinding.ItemRecommendSongBinding;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;

public class RecommendViewHolder extends RecyclerView.ViewHolder {

    ItemRecommendSongBinding binding;
    ItemRecommendViewModel viewModel;
    public RecommendViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemRecommendSongBinding.bind(itemView);
    }

    public void onBind(Song song, int position, AppDataManager appDataManager){
//        viewModel = new ItemRecommendViewModel(appDataManager, song, position);
//        binding.setViewModel(viewModel);
//        binding.executePendingBindings();
    }
}
