package com.example.musicapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ItemFavoriteSongBinding;
import com.example.musicapp.models.Song;

import java.util.List;

public class FavoriteSongAdapter extends RecyclerView.Adapter<FavoriteSongAdapter.ViewHolder> {

    private List<Song> listSong;
    private FavoritesSongListener listener;

    public FavoriteSongAdapter(List<Song> listSong, FavoritesSongListener listener) {
        this.listSong = listSong;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteSongBinding binding = ItemFavoriteSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = listSong.get(position);

        if(song != null) {

            Glide.with(holder.binding.pictureSong.getContext())
                    .load(String.valueOf(song.getArtist().getPicture()))
                    .override(120, 120)
                    .into(holder.binding.pictureSong);

            holder.binding.titleSong.setText(String.valueOf(song.getTitle()));
            holder.binding.artistSong.setText(String.valueOf(song.getArtist().getName()));

            holder.binding.layoutFavoritesSong.setOnClickListener(view -> {
                listener.onClickItem(position);
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
        ItemFavoriteSongBinding binding;

        public ViewHolder(@NonNull ItemFavoriteSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface FavoritesSongListener{
        void onClickItem(int position);
    }
}
