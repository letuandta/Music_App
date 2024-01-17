package com.example.musicapp.ui.recommend;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.MyApplication;
import com.example.musicapp.databinding.ItemSongBinding;
import com.example.musicapp.models.Song;

public class RecommendViewHolder extends RecyclerView.ViewHolder {

    ItemSongBinding binding;
    public RecommendViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemSongBinding.bind(itemView);
    }

    public void onBind(Song song, int position, RecommendSongAdapter.RecommendsSongListener listener){
        Glide.with(binding.pictureSongRecommend.getContext())
                .load(String.valueOf(song.getArtist().getPicture()))
                .override(50, 50)
                .into(binding.pictureSongRecommend);

        binding.titleSongRecommend.setText(String.valueOf(song.getTitle()));
        binding.artistSongRecommend.setText(String.valueOf(song.getArtist().getName()));

        binding.btnAddIntoFavorite.setOnClickListener(view -> {
            try {
                MyApplication.mFavoritesRepository.addSong(song);
                Toast.makeText(view.getContext(), "Add song into favorites list success", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Log.e("FAVORITES LIST", "can't add song into favorites list");
            }
        });

        binding.layoutRecommendsSong.setOnClickListener(view -> {
            listener.onClickRecommendItem(position);
        });

    }
}
