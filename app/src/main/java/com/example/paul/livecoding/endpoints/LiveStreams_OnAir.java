package com.example.paul.livecoding.endpoints;

import com.example.paul.livecoding.oauthentication.AccessToken;
import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAir;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LiveStreams_OnAir {

    @GET("v1/api/livestreams/onair/?format=json")
    Call<List<LiveStreamsOnAir>> getData();
}
