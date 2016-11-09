package com.example.paul.livecoding.Endpoints;

import com.example.paul.livecoding.Activities.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LiveStreams_OnAir {

    @GET("v1/api/livestreams/onair/?format=json")
    Call<List<com.example.paul.livecoding.POJOs.LiveStreams_OnAir>> getData()(@Header("stored_token")
    LoginActivity.stored_token);
}
