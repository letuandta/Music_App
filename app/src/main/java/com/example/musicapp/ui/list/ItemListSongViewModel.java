package com.example.musicapp.ui.list;

import static com.example.musicapp.utils.AppConstants.MusicBundleKey.KEY_SEARCH;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.POSITION;
import static com.example.musicapp.utils.AppConstants.MusicBundleKey.TYPE;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.FAVORITES_SONG;
import static com.example.musicapp.utils.AppConstants.MusicPlayerType.SEARCH_SONG_OFFLINE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableField;

import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.ui.player.MusicPlayerActivity;
import com.example.musicapp.utils.NetworkUtils;

import java.io.IOException;

public class ItemListSongViewModel {
    private ObservableField<Song> song = new ObservableField<>();;
    private String type;
    private String searchKey;
    private int position;

    public ItemListSongViewModel(Song song, String type, String searchKey, int position) {
        this.song.set(song);
        this.searchKey = searchKey;
        this.type = type;
        this.position = position;
    }

    public ObservableField<Song> getSong() {
        return song;
    }

    public void onClickItem(View view){
        try {
            Bundle bundle = new Bundle();

            bundle.putString(TYPE, type);
            bundle.putInt(POSITION, position);
            bundle.putString(KEY_SEARCH, searchKey);

            Intent intent = new Intent(view.getContext(), MusicPlayerActivity.class);
            intent.putExtras(bundle);
            if(NetworkUtils.isConnected() || type.equals(FAVORITES_SONG) || type.equals(SEARCH_SONG_OFFLINE)){
                view.getContext().startActivity(intent);
            }else {
                Toast.makeText(view.getContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}