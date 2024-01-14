package com.example.musicapp.Api;

import com.example.musicapp.models.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface SongService {


    SongService callApi = ApiClient.getClient().create(SongService.class);

   @GET("/search")
   @Headers({
         "X-RapidAPI-Key: 0ae3b525fcmshd91376150aa78f2p15c64fjsnb190213784b2",
          "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"
  })
   Call<Data> getSong(@Query("q") String queryKey);
}
