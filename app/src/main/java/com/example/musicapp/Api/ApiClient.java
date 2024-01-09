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

public class ApiClient {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final String BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

//    @GET("/search")
//    @Headers({
//            "X-RapidAPI-Key: 0ae3b525fcmshd91376150aa78f2p15c64fjsnb190213784b2",
//            "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"
//    })
//    Call<Data> getSong(@Query("q") String queryKey);
}
