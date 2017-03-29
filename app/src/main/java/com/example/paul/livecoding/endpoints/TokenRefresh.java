package com.example.paul.livecoding.endpoints;

import com.example.paul.livecoding.pojo.RefreshAccessToken;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenRefresh {

    @FormUrlEncoded
    @POST("o/token")
    Call<RefreshAccessToken> getNewAccessToken(
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("o/token")
    Call<RefreshAccessToken> getRefreshAccessToken(
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType);

}


















