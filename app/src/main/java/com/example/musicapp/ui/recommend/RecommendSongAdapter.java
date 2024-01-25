package com.example.musicapp.ui.recommend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.musicapp.R;
import com.example.musicapp.data.AppDataManager;
import com.example.musicapp.utils.DiffCallbackUtils;
import com.example.musicapp.data.model.local.Song;

public class RecommendSongAdapter extends ListAdapter<Song, RecommendViewHolder> {

    AppDataManager appDataManager;
    public RecommendSongAdapter(AppDataManager appDataManager){
        super(DiffCallbackUtils.DIFF_CALLBACK_SONG);
        this.appDataManager = appDataManager;
    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_song, parent, false);
        return new RecommendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendViewHolder holder, int position) {
        Song song = getItem(position);
        if(song != null) {
            holder.onBind(song, position, appDataManager);
        }
    }

    @Override
    public int getItemCount() {
        if (getCurrentList().size() < 8) return getCurrentList().size();
        return 7;
    }

}
