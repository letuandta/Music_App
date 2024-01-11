package com.example.musicapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Artist extends RealmObject implements Serializable {

    @PrimaryKey
    private long _id;
    @SerializedName("picture_medium")
    private String picture;

    private String name;

    public Artist() {
    }

    public Artist(String picture, String name) {
        this.picture = picture;
        this.name = name;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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
