package com.example.musicapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ItemRecommendSongBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.repositories.FavoritesRepository;

import java.util.List;

public class RecommendSongAdapter extends RecyclerView.Adapter<RecommendSongAdapter.ViewHolder>{

    private List<Song> listSong;
    private RecommendsSongListener listener;

    public RecommendSongAdapter(List<Song> listSong, RecommendsSongListener listener) {
        this.listSong = listSong;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecommendSongBinding binding = ItemRecommendSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = listSong.get(position);

        if(song != null) {

            Glide.with(holder.binding.pictureSongRecommend.getContext())
                    .load(String.valueOf(song.getArtist().getPicture()))
                    .override(50, 50)
                    .into(holder.binding.pictureSongRecommend);

            holder.binding.titleSongRecommend.setText(String.valueOf(song.getTitle()));
            holder.binding.artistSongRecommend.setText(String.valueOf(song.getArtist().getName()));

            holder.binding.btnAddIntoFavorite.setOnClickListener(view -> {
               try {
                   FavoritesRepository.addSong(song);
                   Toast.makeText(view.getContext(), "Add song into favorites list success", Toast.LENGTH_LONG).show();
               }catch (Exception e){
                   Log.e("FAVORITES LIST", "can't add song into favorites list");
               }
            });

            holder.binding.layoutRecommendsSong.setOnClickListener(view -> {
                listener.onClickRecommendItem(position);
            });

        }
    }

    @Override
    public int getItemCount() {
        if (listSong != null && listSong.size() < 8) return listSong.size();
        if(listSong == null) return 0;
        return 7;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemRecommendSongBinding binding;

        public ViewHolder(@NonNull ItemRecommendSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface RecommendsSongListener{
        void onClickRecommendItem(int position);
    }
}
