package com.example.musicapp.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class InternetConnection {

//    public static CompletableFuture<Boolean> isConnected(Context context) {
//        CompletableFuture<Boolean> checkConnect = new CompletableFuture<>();
//        if(context != null) {
//            ConnectivityManager cm = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//            if (activeNetwork != null && activeNetwork.isConnected()) {
//                try {
//                    URL url = new URL("http://www.google.com/");
//                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//                    urlc.setRequestProperty("User-Agent", "test");
//                    urlc.setRequestProperty("Connection", "close");
//                    urlc.setConnectTimeout(1000); // mTimeout is in seconds
//                    urlc.connect();
//                    checkConnect.complete(urlc.getResponseCode() == 200);
//                } catch (IOException e) {
//                    Log.i("warning", "Error checking internet connection", e);
//                    checkConnect.complete(false);
//                }
//            }
//        }
//        return checkConnect;
//    }

    public static boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}
