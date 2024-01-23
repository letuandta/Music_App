package com.example.musicapp.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.example.musicapp.data.model.local.Song;

import java.io.IOException;

public class NetworkUtils {
    public static void downloadMp3(Context context, Song song){
        String fileName = song.getId() + ".mp3";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(song.getPreview()));
        request.setDescription("Selected Video is being downloaded");
        request.allowScanningByMediaScanner();
        request.setTitle("Downloading Video");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager manager = (DownloadManager)
                context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public static boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}
