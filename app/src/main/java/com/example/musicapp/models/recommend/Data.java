package com.example.musicapp.models.recommend;

import com.example.musicapp.models.Song;
import com.example.musicapp.models.recommend.RecommendSong;
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
