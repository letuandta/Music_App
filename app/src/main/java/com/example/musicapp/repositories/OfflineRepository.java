package com.example.musicapp.repositories;

import android.content.Context;
import android.os.Environment;

import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.utils.NetworkUtils;

import java.io.File;

public class OfflineRepository {
    public void downloadSong(Context context, Song song){
        try {
            NetworkUtils.downloadMp3(context, song);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public File getSong(Context context, String fileName){
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),fileName);
    }

}
