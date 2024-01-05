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
import com.example.musicapp.models.Song;

import java.util.List;

public class FavoriteSongAdapter extends RecyclerView.Adapter<FavoriteSongAdapter.ViewHolder> {

    private List<Song> listSong;

    public FavoriteSongAdapter(List<Song> listSong) {
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = listSong.get(position);

        if(song != null) {

            Glide.with(holder.pictureSong.getContext())
                    .load(String.valueOf(song.getArtist().getPicture()))
                    .override(120, 120)
                    .into(holder.pictureSong);

            holder.titleSong.setText(String.valueOf(song.getTitle()));
            holder.artistSong.setText(String.valueOf(song.getArtist().getName()));
        }
    }

    @Override
    public int getItemCount() {
        if (listSong != null && listSong.size() < 8) return listSong.size();
        if(listSong == null) return 0;
        return 7;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView pictureSong;
        public TextView titleSong;
        public TextView artistSong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pictureSong = itemView.findViewById(R.id.picture_song);
            titleSong = itemView.findViewById(R.id.title_song);
            artistSong = itemView.findViewById(R.id.artist_song);
        }
    }
}
