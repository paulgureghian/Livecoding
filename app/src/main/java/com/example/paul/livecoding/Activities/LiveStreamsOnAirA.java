package com.example.paul.livecoding.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.paul.livecoding.Deserializers.LiveStreamsOnAirD;
import com.example.paul.livecoding.Endpoints.LiveStreamsOnAirE;
import com.example.paul.livecoding.R;
import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStreamsOnAirA extends AppCompatActivity implements Callback<List<LiveStreamsOnAirP>> {

    String access_token;
    SharedPreferences pref;
    List<LiveStreamsOnAirP> items;
    Type listType = new TypeToken<List<LiveStreamsOnAirP>>() {}.getType();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestreams);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        this.setTitle(getResources().getString(R.string.live_streams_on_air));

        pref = getSharedPreferences("access_token", MODE_PRIVATE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.livecoding.tv/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new LiveStreamsOnAirD()).create()))
                .build();
        LiveStreamsOnAirE liveStreams_onAir = retrofit.create(LiveStreamsOnAirE.class);

        access_token = pref.getString("stored_token", access_token);
        Log.e("livestreams_accesstoken", access_token);

        Call<List<LiveStreamsOnAirP>> call = liveStreams_onAir.getData(access_token);
        call.enqueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            Toast.makeText(this, context.getResources().getString(R.string.connection_made), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, context.getResources().getString(R.string.no_connection_made) + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<List<LiveStreamsOnAirP>> call, Throwable t) {
        Toast.makeText(this, context.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
    }
}







































































































































