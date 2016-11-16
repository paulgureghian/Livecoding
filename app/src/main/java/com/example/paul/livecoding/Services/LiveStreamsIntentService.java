package com.example.paul.livecoding.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.widget.Toast;

import com.example.paul.livecoding.Deserializers.LiveStreamsOnAirD;
import com.example.paul.livecoding.Endpoints.LiveStreamsOnAirE;
import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;
import com.example.paul.livecoding.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.paul.livecoding.Activities.LoginActivity.access_token;

public class LiveStreamsIntentService extends IntentService {

    String access_token;
    SharedPreferences pref;
    List<LiveStreamsOnAirP> items;
    Type listType = new TypeToken<List<LiveStreamsOnAirP>>() {  }.getType();

    public LiveStreamsIntentService() {
        super("LiveStreamsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        initDownload();
    }

    private void initDownload() {

        pref = getSharedPreferences("access_token", MODE_PRIVATE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.livecoding.tv/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new LiveStreamsOnAirD()).create()))
                .client(httpClient.build())
                .build();
        LiveStreamsOnAirE liveStreams_onAir = retrofit.create(LiveStreamsOnAirE.class);

        access_token = pref.getString("access_token", access_token);
        Log.e("livestreams_accesstoken", access_token);

        Call<List<LiveStreamsOnAirP>> call = liveStreams_onAir.getData(access_token);

    }
}




























