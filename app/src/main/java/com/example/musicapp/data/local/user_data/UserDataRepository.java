package com.example.musicapp.data.local.user_data;
import com.example.musicapp.data.model.local.Song;

import java.io.File;

public interface UserDataRepository {
    void downloadSong(Song song);
    File getSong(String fileName);

}
