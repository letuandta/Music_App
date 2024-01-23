package com.example.musicapp.data.local.user_data;

import android.content.Context;
import android.os.Environment;

import com.example.musicapp.data.model.local.Song;
import com.example.musicapp.utils.NetworkUtils;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppUserDataRepository implements UserDataRepository{

    Context context;
    @Inject public AppUserDataRepository(Context context) {
        this.context = context;
    }

    @Override
    public void downloadSong(Song song) {
        try {
            NetworkUtils.downloadMp3(context, song);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public File getSong(String fileName) {
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),fileName);
    }
}
