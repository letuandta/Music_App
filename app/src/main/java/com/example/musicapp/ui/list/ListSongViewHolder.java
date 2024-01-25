package com.example.musicapp.ui.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.databinding.ItemListSongBinding;
import com.example.musicapp.data.model.local.Song;

public class ListSongViewHolder extends RecyclerView.ViewHolder {

    ItemListSongBinding binding;
    ItemListSongViewModel viewModel;
    public ListSongViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemListSongBinding.bind(itemView);
    }
    public void onBind(Song song, String type, String searchKey, int position){
        viewModel = new ItemListSongViewModel(song, type, searchKey, position);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
    }
}
