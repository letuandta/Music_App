package com.example.musicapp.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.musicapp.R;
import com.example.musicapp.common.DiffCallback;
import com.example.musicapp.models.Song;

public class ListSongAdapter extends ListAdapter<Song, ListSongViewHolder> {

    ListSongListener listener;
    protected ListSongAdapter(ListSongListener listener) {
        super(DiffCallback.DIFF_CALLBACK_SONG);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ListSongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSongViewHolder holder, int position) {
        holder.onBind(getItem(position), position, listener);
    }

    public interface ListSongListener{
        void onClickItem(int position);
    }
}
