package com.example.paul.livecoding.activivities_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.paul.livecoding.R;
import com.example.paul.livecoding.endpoints.LiveStreams_OnAir;
import com.example.paul.livecoding.oauthentication.AccessToken;
import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAir;
import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAirDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.paul.livecoding.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements Callback<List<LiveStreamsOnAir>> {

    List<LiveStreamsOnAir> items;
    Type listType = new TypeToken<List<LiveStreamsOnAir>>() { }.getType();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.livecoding.tv/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new LiveStreamsOnAirDeserializer()).create()))
                .build();
        LiveStreams_OnAir liveStreams_onAir = retrofit.create(LiveStreams_OnAir.class);
        Call<List<LiveStreamsOnAir>> call = liveStreams_onAir.getData();
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
            (Call<List<LiveStreamsOnAir>> call, Response<List<LiveStreamsOnAir>> response) {

        items = response.body();
        int code = response.code();
        List<LiveStreamsOnAir> items = response.body();

        Log.d("res", response.raw().toString());

        for (LiveStreamsOnAir item : items) {
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
    public void onFailure(Call<List<LiveStreamsOnAir>> call, Throwable t) {
        Toast.makeText(this, context.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
    }
}







































































































































