package com.example.musicapp.common;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.musicapp.models.Song;

public class DiffCallback {

    public static final DiffUtil.ItemCallback<Song> DIFF_CALLBACK_SONG =
            new DiffUtil.ItemCallback<Song>() {
                @Override
                public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
