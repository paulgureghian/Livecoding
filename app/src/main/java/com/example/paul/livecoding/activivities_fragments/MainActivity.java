package com.example.paul.livecoding.activivities_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abozaid.oauth2library.Controller.Controller;
import com.abozaid.oauth2library.Model.Credential;
import com.abozaid.oauth2library.OAuthToken;
import com.example.paul.livecoding.R;
import com.example.paul.livecoding.endpoints.LiveStreams_OnAir;
import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAir;
import com.example.paul.livecoding.pojo_deserializer.LiveStreamsOnAirDeserializer;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity implements Callback<List<LiveStreamsOnAir>> {

    OAuthToken oAuthToken = new OAuthToken("https://www.livecoding.tv", "kBadIfKZYpgLTowhMreDXJ5ckyGz7t8BxUdIJ2XC", "yfhYWhsXkjct9R2bfHYVMGcdEQWNT7Plc99MYx98ejlJpc90Mj2hky3ADOPWeyTZz43KrjGrkQLEPUawkZnsmFfxm8RZQzqVZuQ4SxtdISBrDAqbjt1OWuv60LEBDL7R", this);
    Credential credential = oAuthToken.getAccessTokenWithID("default");

    List<LiveStreamsOnAir> items;
    Type listType = new TypeToken<List<LiveStreamsOnAir>>() {
    }.getType();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        AuthenticateUsingOAuth();

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.livecoding.tv")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new LiveStreamsOnAirDeserializer()).create()))
                .build();
        LiveStreams_OnAir liveStreams_onAir = retrofit.create(LiveStreams_OnAir.class);
        Call<List<LiveStreamsOnAir>> call = liveStreams_onAir.getData();
        call.enqueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    public void onResponse(Call<List<LiveStreamsOnAir>> call, Response<List<LiveStreamsOnAir>> response) {

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

    public void AuthenticateUsingOAuth() {
        if (credential == null || credential.getAccess_token() == null) {
            oAuthToken.authenticateUsingOAuth("v1/api/oauth", "5592b3be0cf2921ec587d5)",
                    "935eadc6-cd58-4743-8f14-5f7af391c992", "read write", "password",
                    new Controller.MethodsCallback<Credential>() {
                        @Override
                        public void failure(Throwable throwable) {

                        }

                        @Override
                        public void success(Credential credential) {

                        }

                        @Override
                        public void responseBody(Call call) {

                        }
                    });
        }
    }
}