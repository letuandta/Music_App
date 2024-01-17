package com.example.musicapp.ui.favorite;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.databinding.ItemFavoriteSongBinding;
import com.example.musicapp.models.Song;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    ItemFavoriteSongBinding binding;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.binding = ItemFavoriteSongBinding.bind(itemView);
    }

    public void onBind(@NonNull Song song, int position, FavoriteSongAdapter.FavoritesSongListener listener){
        Glide.with(binding.pictureSong.getContext())
                .load(String.valueOf(song.getArtist().getPicture()))
                .override(120, 120)
                .into(binding.pictureSong);

        binding.titleSong.setText(String.valueOf(song.getTitle()));
        binding.artistSong.setText(String.valueOf(song.getArtist().getName()));

        binding.layoutFavoritesSong.setOnClickListener(view -> {
            listener.onClickItem(position);
        });
    }
}
