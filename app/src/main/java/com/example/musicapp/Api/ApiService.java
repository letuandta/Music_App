package com.example.musicapp.Api;

import com.example.musicapp.models.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("/search")
    @Headers({
            "X-RapidAPI-Key: 0ae3b525fcmshd91376150aa78f2p15c64fjsnb190213784b2",
            "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"
    })
    Call<Data> getSong(@Query("q") String queryKey);
}
