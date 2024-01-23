package com.example.musicapp.utils;

public class TimeFormat {
    public static String formattedTime(int currentDuration) {
        String time;
        String seconds = String.valueOf(currentDuration % 60);
        String minutes = String.valueOf(currentDuration / 60);
        time = minutes + ":" + seconds;
        return time;
    }
}
