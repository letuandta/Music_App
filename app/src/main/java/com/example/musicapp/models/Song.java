package com.example.musicapp.models;

public class Song {
    private String title;
    private int duration;
    private String preview;
    private String picture;
    private String artist;

    public Song() {
    }

    public Song(String title, int duration, String preview, String picture, String artist) {
        this.title = title;
        this.duration = duration;
        this.preview = preview;
        this.picture = picture;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
