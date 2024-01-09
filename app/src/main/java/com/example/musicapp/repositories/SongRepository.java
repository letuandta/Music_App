package com.example.musicapp.repositories;

import com.example.musicapp.Api.ApiClient;
import com.example.musicapp.models.Data;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface SongRepository {


    SongRepository callApi = ApiClient.getClient().create(SongRepository.class);

   @GET("/search")
   @Headers({
         "X-RapidAPI-Key: 0ae3b525fcmshd91376150aa78f2p15c64fjsnb190213784b2",
          "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"
  })
   Call<Data> getSong(@Query("q") String queryKey);
}
