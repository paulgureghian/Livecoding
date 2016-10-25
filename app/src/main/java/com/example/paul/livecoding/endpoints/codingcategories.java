package com.example.paul.livecoding.endpoints;

import com.example.paul.livecoding.pojo.Categories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CodingCategories {

    @GET("/api/CodingCategories")
    Call<List<Categories>> getData();
}
