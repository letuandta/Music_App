package com.example.musicapp.ui.list;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.MyApplication;
import com.example.musicapp.databinding.ItemSongBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.ui.recommend.RecommendSongAdapter;

public class ListSongViewHolder extends RecyclerView.ViewHolder {

    ItemSongBinding binding;
    public ListSongViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemSongBinding.bind(itemView);
    }

    public void onBind(Song song, int position, ListSongAdapter.ListSongListener listener){
        Glide.with(binding.pictureSongRecommend.getContext())
                .load(String.valueOf(song.getArtist().getPicture()))
                .override(50, 50)
                .into(binding.pictureSongRecommend);

        binding.titleSongRecommend.setText(String.valueOf(song.getTitle()));
        binding.artistSongRecommend.setText(String.valueOf(song.getArtist().getName()));

        binding.btnAddIntoFavorite.setVisibility(View.INVISIBLE);

        binding.layoutRecommendsSong.setOnClickListener(view -> {
            listener.onClickItem(position);
        });

    }
}
