package com.example.musicapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
    @SerializedName("title")
    private String title;
    @SerializedName("duration")
    private int duration;
    @SerializedName("preview")
    private String preview;
    @SerializedName("artist")
    @Expose
    private Artist artist;

    public Song() {
    }

    public Song(String title, int duration, String preview, Artist artist) {
        this.title = title;
        this.duration = duration;
        this.preview = preview;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }



    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
