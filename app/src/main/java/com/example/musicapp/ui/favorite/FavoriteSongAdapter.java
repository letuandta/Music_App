package com.example.musicapp.ui.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.musicapp.R;
import com.example.musicapp.utils.DiffCallbackUtils;
import com.example.musicapp.data.model.local.Song;

public class FavoriteSongAdapter extends ListAdapter<Song, FavoriteViewHolder> {

    public FavoriteSongAdapter() {
        super(DiffCallbackUtils.DIFF_CALLBACK_SONG);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_song, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        if (getCurrentList().size() < 8) return getCurrentList().size();
        return 7;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Song song = getItem(position);
        if(song != null) {
            holder.onBind(song, position);
        }
    }
}
