package com.example.musicapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist {
    @SerializedName("picture_medium")
    @Expose
    private String picture;
    @SerializedName("name")
    @Expose
    private String name;

    public Artist() {
    }

    public Artist(String picture, String name) {
        this.picture = picture;
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
