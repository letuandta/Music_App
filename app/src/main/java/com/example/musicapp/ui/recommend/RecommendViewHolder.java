package com.example.musicapp.ui.recommend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.MyApplication;
import com.example.musicapp.databinding.ItemSongBinding;
import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;

public class RecommendViewHolder extends RecyclerView.ViewHolder {

    ItemSongBinding binding;
    Context context;
    public RecommendViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        binding = ItemSongBinding.bind(itemView);
        this.context = context;
    }

    public void onBind(Song song, int position, RecommendSongAdapter.RecommendsSongListener listener){
        Glide.with(binding.pictureSongRecommend.getContext())
                .load(String.valueOf(song.getArtist().getPicture()))
                .override(50, 50)
                .into(binding.pictureSongRecommend);

        binding.titleSongRecommend.setText(String.valueOf(song.getTitle()));
        binding.artistSongRecommend.setText(String.valueOf(song.getArtist().getName()));
        try {
            if (!NetworkUtils.isConnected()){
                binding.btnAddIntoFavorite.setVisibility(View.INVISIBLE);
            }
            else {
                binding.btnAddIntoFavorite.setOnClickListener(view -> {
                    try {
                        if(!MyApplication.mFavoritesRepository.exists(song.getId()) && NetworkUtils.isConnected()) {
                            MyApplication.mFavoritesRepository.addSong(song);
                            String fileName = song.getId() + ".mp3";
                            File file = MyApplication.mOfflineRepository.getSong(context, fileName);
                            if (!file.exists())
                                MyApplication.mOfflineRepository.downloadSong(context, song);
                            Toast.makeText(view.getContext(), "Add song into favorites list success", Toast.LENGTH_LONG).show();
                            binding.btnAddIntoFavorite.setVisibility(View.INVISIBLE);
                        }
                        else Toast.makeText(view.getContext(), "This song already exists OR no internet", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                            Log.e("FAVORITES LIST", "can't add song into favorites list");
                    }
                });
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        binding.layoutRecommendsSong.setOnClickListener(view -> {
            listener.onClickRecommendItem(position);
        });

    }
}
