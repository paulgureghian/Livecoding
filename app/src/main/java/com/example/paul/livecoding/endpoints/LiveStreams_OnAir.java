package com.example.paul.livecoding.Endpoints;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LiveStreams_OnAir {

    @GET("v1/api/livestreams/onair/?format=json")
    Call<List<com.example.paul.livecoding.POJOs.LiveStreams_OnAir>> getData();
}
