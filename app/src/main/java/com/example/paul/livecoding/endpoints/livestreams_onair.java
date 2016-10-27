package com.example.paul.livecoding.endpoints;

import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAir;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LiveStreams_OnAir {

    @GET("/api/livestreams/onair/?format=json")
    Call<List<LiveStreamsOnAir>> getData();
}
