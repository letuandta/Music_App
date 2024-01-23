package com.example.musicapp.data.model.local;

import androidx.annotation.Nullable;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RecommendSong implements Serializable, RealmModel {
    @PrimaryKey
    private String id;

    private String title;

    private int duration;

    private String preview;

    private Artist artist;

    public RecommendSong() {
    }

    public RecommendSong(String title, int duration, String preview, Artist artist) {
        this.title = title;
        this.duration = duration;
        this.preview = preview;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        final RecommendSong other = (RecommendSong) obj;
        return this.id.equals(other.getId());
    }


}
