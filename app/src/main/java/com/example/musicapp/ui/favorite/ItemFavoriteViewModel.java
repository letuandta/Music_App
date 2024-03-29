package com.example.musicapp.ui.favorite;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.SONG_ID;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.ObservableField;

import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.ui.player.MusicPlayerActivity;

import java.util.Objects;

public class ItemFavoriteViewModel{
    private final ObservableField<Song> song = new ObservableField<>();
    private int position;

    public ItemFavoriteViewModel(Song song, int position) {
        this.song.set(song);
        this.position = position;
    }

    public ObservableField<Song> getSong() {
        return song;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void onClickItem(View view){
        Bundle bundle = new Bundle();

        bundle.putString(TYPE, FAVORITES_SONG);
        bundle.putString(SONG_ID, Objects.requireNonNull(song.get()).getId());

        Intent intent = new Intent(view.getContext(), MusicPlayerActivity.class);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
