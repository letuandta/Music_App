package com.example.musicapp.ui.favorite;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.databinding.ItemFavoriteSongBinding;
import com.example.musicapp.data.model.local.Song;


public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    ItemFavoriteSongBinding binding;
    ItemFavoriteViewModel viewModel;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.binding = ItemFavoriteSongBinding.bind(itemView);

    }

    public void onBind(@NonNull Song song, int position){
        viewModel = new ItemFavoriteViewModel(song, position);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
    }
}
