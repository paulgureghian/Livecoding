package com.example.paul.livecoding.endpoints;

import com.example.paul.livecoding.pojo_deserializer.Categories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CodingCategories {

    @GET("/api/codingcategories/")
    Call<List<Categories>> getData();
}
