package com.example.paul.livecoding.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.paul.livecoding.R;
import com.example.paul.livecoding.POJOs.LiveStreams_OnAir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStreamsOnAir extends AppCompatActivity implements Callback<List<LiveStreams_OnAir>> {

    List<LiveStreams_OnAir> items;
    Type listType = new TypeToken<List<LiveStreams_OnAir>>() { }.getType();

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

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.livecoding.tv/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new com.example.paul.livecoding.Deserializers.LiveStreamsOnAir()).create()))
                .build();
        com.example.paul.livecoding.Endpoints.LiveStreams_OnAir liveStreams_onAir = retrofit.create(com.example.paul.livecoding.Endpoints.LiveStreams_OnAir.class);
        Call<List<LiveStreams_OnAir>> call = liveStreams_onAir.getData();
//        call.enqueue(this);
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
            (Call<List<LiveStreams_OnAir>> call, Response<List<LiveStreams_OnAir>> response) {

        items = response.body();
        int code = response.code();
        List<LiveStreams_OnAir> items = response.body();

        Log.d("res", response.raw().toString());

        for (LiveStreams_OnAir item : items) {
            Log.i("item", item.getUser());
        }
        if (code == 200) {

            Toast.makeText(this, context.getResources().getString(R.string.connection_made), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, context.getResources().getString(R.string.no_connection_made) + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<List<LiveStreams_OnAir>> call, Throwable t) {
        Toast.makeText(this, context.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
    }
}







































































































































