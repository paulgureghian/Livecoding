package com.example.paul.livecoding.Endpoints;

import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LiveStreamsOnAirE {

    @GET("api/livestreams/onair/?format=json")
    Call<List<LiveStreamsOnAirP>> getData(@Query("access_token") String access_token);
}



