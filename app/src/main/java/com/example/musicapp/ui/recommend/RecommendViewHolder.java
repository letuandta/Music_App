package com.example.musicapp.ui.recommend;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.databinding.ItemRecommendSongBinding;
import com.example.musicapp.data.model.local.Song;

public class RecommendViewHolder extends RecyclerView.ViewHolder {

    ItemRecommendSongBinding binding;
    ItemRecommendViewModel viewModel;
    public RecommendViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemRecommendSongBinding.bind(itemView);
    }

    public void onBind(Song song, int position, AppDataManager appDataManager){
        viewModel = new ItemRecommendViewModel(appDataManager, song, position);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
    }
}
