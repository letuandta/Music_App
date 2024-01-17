package com.example.musicapp.ui.recommend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.musicapp.R;
import com.example.musicapp.common.DiffCallback;
import com.example.musicapp.models.Song;

public class RecommendSongAdapter extends ListAdapter<Song, RecommendViewHolder> {
    private RecommendsSongListener listener;

    public RecommendSongAdapter(RecommendsSongListener listener){
        super(DiffCallback.DIFF_CALLBACK_SONG);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new RecommendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendViewHolder holder, int position) {
        Song song = getItem(position);
        if(song != null) {
            holder.onBind(song, position, listener);
        }
    }

    @Override
    public int getItemCount() {
        if (getCurrentList().size() < 8) return getCurrentList().size();
        return 7;
    }


    public interface RecommendsSongListener{
        void onClickRecommendItem(int position);
    }
}
