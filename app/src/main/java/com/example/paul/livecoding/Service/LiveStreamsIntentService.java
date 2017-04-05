package com.example.paul.livecoding.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.paul.livecoding.BuildConfig;
import com.example.paul.livecoding.MyApplication;
import com.example.paul.livecoding.R;
import com.example.paul.livecoding.database.StreamsColumns;
import com.example.paul.livecoding.database.StreamsProvider;
import com.example.paul.livecoding.deserializers.LiveStreamsOnAirD;
import com.example.paul.livecoding.endpoints.LiveStreamsOnAirE;
import com.example.paul.livecoding.endpoints.TokenRefresh;
import com.example.paul.livecoding.eventbus.Reload;
import com.example.paul.livecoding.pojo.LiveStreamsOnAirP;
import com.example.paul.livecoding.pojo.RefreshAccessToken;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStreamsIntentService extends IntentService implements Callback<List<LiveStreamsOnAirP>> {

    String access_token;
    String refresh_token;
    SharedPreferences pref;
    List<LiveStreamsOnAirP> items;
    Type listType = new TypeToken<List<LiveStreamsOnAirP>>() {}.getType();

    public LiveStreamsIntentService() {
        super("LiveStreamsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        initDownload();

        access_token = MyApplication.preferences.getString("access_token",access_token);
        refresh_token = MyApplication.preferences.getString("refresh_token", refresh_token);

    }

    public class TokenAuthenticator implements Authenticator {

        @Override
        public Request authenticate(Route route, okhttp3.Response response) throws IOException {

            // If the request fails with 401, call getRefreshAccessToken() to renew the access_token

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.liveedu.tv/")
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();

            TokenRefresh service = retrofit.create(TokenRefresh.class);

            Call<RefreshAccessToken> call = service.getRefreshAccessToken(refresh_token, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET,"http://localhost","refresh_token");
            RefreshAccessToken refreshAccessToken = call.execute().body();

            // Add new header to rejected request and retry it
            if (refreshAccessToken != null) {
                access_token = refreshAccessToken.getAccessToken();
            }

            // Add a custom Authorization header with the new access token to the request. This should fix the issue
            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + access_token)
                    .build();
        }

    }

    private void initDownload() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        // Add a custom OkHttp3 Authenticator to the request
        httpClient.authenticator(new TokenAuthenticator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.liveedu.tv/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new LiveStreamsOnAirD()).create()))
                .client(httpClient.build())
                .build();
        LiveStreamsOnAirE liveStreams_onAir = retrofit.create(LiveStreamsOnAirE.class);

        access_token = MyApplication.preferences.getString("access_token",access_token);
        refresh_token = MyApplication.preferences.getString("refresh_token", refresh_token);

        Log.e("livestream_accesstoken", access_token);
        Log.e("livestream_refreshtoken",refresh_token);

        Call<List<LiveStreamsOnAirP>> call = liveStreams_onAir.getData(access_token);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<LiveStreamsOnAirP>> call, Response<List<LiveStreamsOnAirP>> response) {

        items = response.body();
        int code = response.code();
        List<LiveStreamsOnAirP> items = response.body();

        Log.e("response", response.raw().toString());

        getContentResolver().delete(StreamsProvider.Streams.CONTENT_URI, null, null);

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

            Log.i("getViewingUrls()", item.getViewingUrls().size()+"");
            Log.e("viewing urls1", String.valueOf(item.getViewingUrls()));
            Log.e("title", item.getTitle());
            Log.e("items", item.getUser());
            Log.e("content_values", String.valueOf(contentValues));
        }

        if (code == 200) {
            Toast.makeText(this, getResources().getString(R.string.ready), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_connection_made), Toast.LENGTH_SHORT).show();
        }

        EventBus.getDefault().post(new Reload());
    }

    @Override
    public void onFailure(Call<List<LiveStreamsOnAirP>> call, Throwable t) {
        Toast.makeText(this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }
}




























