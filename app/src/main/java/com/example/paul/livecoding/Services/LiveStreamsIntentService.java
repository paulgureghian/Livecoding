package com.example.paul.livecoding.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.paul.livecoding.Deserializers.LiveStreamsOnAirD;
import com.example.paul.livecoding.Endpoints.LiveStreamsOnAirE;
import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;
import com.example.paul.livecoding.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.paul.livecoding.Activities.LoginActivity.access_token;

public class LiveStreamsIntentService extends IntentService {

    public LiveStreamsIntentService() {
        super("LiveStreamsIntentService");
    }

    private NotificationCompat.Builder notificationbuilder;
    private NotificationManager notificationManager;

    @Override
    protected void onHandleIntent(Intent intent) {

        initDownload();
    }

    private void initDownload() {

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
        call.enqueue(this);
    }
}

    @Override
    public void onResponse
            (Call<List<LiveStreamsOnAirP>> call, Response<List<LiveStreamsOnAirP>> response) {

        items = response.body();
        int code = response.code();
        List<LiveStreamsOnAirP> items = response.body();

        Log.e("res", response.raw().toString());

        for (LiveStreamsOnAirP item : items) {
            Log.e("item", item.getUser());
        }
        if (code == 200) {

            Toast.makeText(this, getResources().getString(R.string.connection_made), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_connection_made) + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<List<LiveStreamsOnAirP>> call, Throwable t) {
        Toast.makeText(this, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
    }
}


























