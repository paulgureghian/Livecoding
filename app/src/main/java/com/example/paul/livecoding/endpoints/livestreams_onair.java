package com.example.paul.livecoding.endpoints;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LiveStreams_OnAir {

    @GET("/api/livestreams/onair")
    Call<List<LiveStreamsOnAir>> getData();
}
