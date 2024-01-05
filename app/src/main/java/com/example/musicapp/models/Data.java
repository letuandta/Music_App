package com.example.musicapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("data")
    private List<Song> data;

    public List<Song> getData() {
        return data;
    }

    public void setData(List<Song> data) {
        this.data = data;
    }
}
