package com.example.musicapp.repositories;

import com.example.musicapp.Api.ApiService;
import com.example.musicapp.models.Data;

import java.io.IOException;


public class SongRepository {


    public static Data getSongs(String queryKey) throws IOException {
        return ApiService.apiService.getSong(queryKey).execute().body();
    }

}
