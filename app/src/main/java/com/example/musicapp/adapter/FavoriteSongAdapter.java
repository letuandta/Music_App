package com.example.musicapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.databinding.ItemFavoriteSongBinding;
import com.example.musicapp.models.Song;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongAdapter extends ListAdapter<Song, FavoriteSongAdapter.ViewHolder> {


    FavoritesSongListener listener;

    public FavoriteSongAdapter(FavoritesSongListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteSongBinding binding = ItemFavoriteSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false); // Should be in ViewModel class
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        if (getCurrentList().size() < 8) return getCurrentList().size();
        return 7;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // Should use method of ViewHolder instead
        Song song = getItem(position);

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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemFavoriteSongBinding binding;

        public ViewHolder(@NonNull ItemFavoriteSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static final DiffUtil.ItemCallback<Song> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Song>() {
                @Override
                public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
                    Log.e("TAG", "areItemsTheSame:" + (oldItem.get_id() == newItem.get_id()));
                    return oldItem.get_id() == newItem.get_id();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public interface FavoritesSongListener{
        void onClickItem(int position);
    }
}
