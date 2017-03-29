package com.example.paul.livecoding.endpoints;

import com.example.paul.livecoding.pojo.LiveStreamsOnAirP;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LiveStreamsOnAirE {

    @GET("api/livestreams/onair/?format=json")
    Call<List<LiveStreamsOnAirP>> getData(@Query("access_token") String access_token);
}



