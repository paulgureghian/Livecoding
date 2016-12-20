package com.example.paul.livecoding.Service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.paul.livecoding.DataBase.StreamsColumns;
import com.example.paul.livecoding.DataBase.StreamsProvider;
import com.example.paul.livecoding.Deserializers.LiveStreamsOnAirD;
import com.example.paul.livecoding.Endpoints.LiveStreamsOnAirE;
import com.example.paul.livecoding.EventBus.Reload;
import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;
import com.example.paul.livecoding.R;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStreamsIntentService extends IntentService implements Callback<List<LiveStreamsOnAirP>> {

    String access_token;
    SharedPreferences pref;
    List<LiveStreamsOnAirP> items;
    Type listType = new TypeToken<List<LiveStreamsOnAirP>>() {}.getType();

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

        EventBus.getDefault().post(new Reload());

    }

    @Override
    public void onResponse(Call<List<LiveStreamsOnAirP>> call, Response<List<LiveStreamsOnAirP>> response) {

        items = response.body();
        int code = response.code();
        List<LiveStreamsOnAirP> items = response.body();

        Log.e("response", response.raw().toString());

        for (LiveStreamsOnAirP item : items) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(StreamsColumns._URL, item.getUrl());
            contentValues.put(StreamsColumns.USER, item.getUser());
            contentValues.put(StreamsColumns.USER_SLUG, item.getUserSlug());
            contentValues.put(StreamsColumns.TITLE, item.getTitle());
            contentValues.put(StreamsColumns.DESCRIPTION, item.getDescription());
            contentValues.put(StreamsColumns.CODING_CATEGORY, item.getCodingCategory());
            contentValues.put(StreamsColumns.DIFFICULTY, item.getDifficulty());
            contentValues.put(StreamsColumns.LANGUAGE, item.getLanguage());
            contentValues.put(StreamsColumns.TAGS, item.getTags());
            contentValues.put(StreamsColumns.IS_LIVE, item.getIsLive());
            contentValues.put(StreamsColumns.VIEWERS_LIVE, item.getViewersLive());
            contentValues.put(StreamsColumns.VIEWING_URLS1, item.getViewingUrls().get(0));
            contentValues.put(StreamsColumns.VIEWING_URLS2, item.getViewingUrls().get(1));
            contentValues.put(StreamsColumns.VIEWING_URLS3, item.getViewingUrls().get(2));
            contentValues.put(StreamsColumns.THUMBNAIL_URL, item.getThumbnailUrl());
            contentValues.put(StreamsColumns.EMBED_URL, item.getEmbedUrl());

            getContentResolver().insert(StreamsProvider.Streams.CONTENT_URI, contentValues);

            Log.e("items", item.getUser());
            Log.e("content_values", String.valueOf(contentValues));
        }

        if (code == 200) {
            Toast.makeText(this, getResources().getString(R.string.ready), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_connection_made), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<List<LiveStreamsOnAirP>> call, Throwable t) {
        Toast.makeText(this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }
}




























