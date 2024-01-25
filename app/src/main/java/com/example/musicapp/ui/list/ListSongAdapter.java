package com.example.musicapp.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.musicapp.R;
import com.example.musicapp.utils.DiffCallbackUtils;
import com.example.musicapp.data.model.local.Song;

public class ListSongAdapter extends ListAdapter<Song, ListSongViewHolder> {

    String type;
    String searchSong;
    protected ListSongAdapter(String type, String searchKey) {
        super(DiffCallbackUtils.DIFF_CALLBACK_SONG);
        this.type = type;
        this.searchSong = searchKey;
    }

    @NonNull
    @Override
    public ListSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_song, parent, false);
        return new ListSongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSongViewHolder holder, int position) {
        holder.onBind(getItem(position), type, searchSong, position);
    }

}
