package com.example.musicapp.data.model.local;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Artist implements Serializable, RealmModel {

    @PrimaryKey
    private String id;
    @SerializedName("picture_medium")
    private String picture;

    private String name;

    public Artist() {
    }

    public Artist(String picture, String name) {
        this.picture = picture;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
