package com.example.paul.livecoding.Endpoints;

import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LiveStreamsOnAirE {

    @GET("v1/api/livestreams/onair/?format=json")
    Call<List<LiveStreamsOnAirP>> getData(@Header("Access-Token") String access_token);
}
