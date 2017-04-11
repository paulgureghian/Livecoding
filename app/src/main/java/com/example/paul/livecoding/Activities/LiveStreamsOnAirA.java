package com.example.paul.livecoding.activities;

import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.paul.livecoding.BuildConfig;
import com.example.paul.livecoding.adapter.StreamsAdapter;
import com.example.paul.livecoding.database.StreamsColumns;
import com.example.paul.livecoding.database.StreamsProvider;
import com.example.paul.livecoding.endpoints.TokenRefresh;
import com.example.paul.livecoding.eventbus.Reload;
import com.example.paul.livecoding.R;
import com.example.paul.livecoding.pojo.RefreshAccessToken;
import com.example.paul.livecoding.recyclerviewlistener.RecyclerViewItemClickListener;
import com.example.paul.livecoding.service.LiveStreamsIntentService;
import com.example.paul.livecoding.sharedprefs.Prefs;
import com.example.paul.livecoding.widget.Widget;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStreamsOnAirA extends AppCompatActivity implements Callback<RefreshAccessToken>, LoaderManager.LoaderCallbacks<Cursor> {

    String access_token;
    String refresh_token;
    String parsedCode;
    String encoded = BuildConfig.CLIENT_ID + ":" + BuildConfig.CLIENT_SECRET;
    String grant_type = "authorization_code";
    String redirect_uri = "http://localhost";
    Intent intent;
    Cursor mCursor;
    Context context;
    Boolean isConnected;
    private MenuItem menuItem;
    public StreamsAdapter streamsAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int CURSOR_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_livestreams);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7912552361212336/5646206405");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1FA7C25AE3312C062587621EE230994F")
                .build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        this.setTitle(getResources().getString(R.string.live_streams_on_air));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        parsedCode = Prefs.preferences.getString("parsed_code", parsedCode);
        Log.e("parsed_code1", parsedCode);
        getNewAccessToken();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        streamsAdapter = new StreamsAdapter(this);
        recyclerView.setAdapter(streamsAdapter);

        intent = new Intent(LiveStreamsOnAirA.this, LiveStreamsIntentService.class);
        if (savedInstanceState == null) {
            if (isConnected) {
                startService(intent);
            } else {
                Toast.makeText(context, getString(R.string.no_connection_made), Toast.LENGTH_SHORT).show();
            }

            recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                    new RecyclerViewItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            intent = new Intent(context, CurrentStream.class);
                            mCursor = streamsAdapter.getCursor();
                            mCursor.moveToPosition(position);

                            int id = mCursor.getInt(mCursor.getColumnIndex(StreamsColumns._ID));
                            String name = mCursor.getString(mCursor.getColumnIndex(StreamsColumns.TITLE));

                            intent.putExtra(StreamsColumns._ID, id);

                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(id));
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LiveStreamsOnAirA.this).toBundle());
                        }
                    }));
        }
    }

    public void getNewAccessToken() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.liveedu.tv/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        TokenRefresh tokenRefresh = retrofit.create(TokenRefresh.class);

        String encoded_id_secret = "Basic " + Base64.encodeToString(encoded.getBytes(), Base64.NO_WRAP);
        Call<RefreshAccessToken> call = tokenRefresh.getNewAccessToken(parsedCode, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, encoded_id_secret, redirect_uri, grant_type);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<RefreshAccessToken> call, Response<RefreshAccessToken> response) {

        RefreshAccessToken refreshAccessToken;

        refreshAccessToken = response.body();

        int code = response.code();

        Log.e("reponse", response.raw().toString());
        Log.e("getAcessToken()", refreshAccessToken.getAccessToken());
        Log.e("getTokenType()", refreshAccessToken.getTokenType());
        Log.e("getExpiry()", String.valueOf(refreshAccessToken.getExpiry()));
        Log.e("getRefreshToken()", refreshAccessToken.getRefreshToken());
        Log.e("getScope)()", refreshAccessToken.getScope());

        access_token = refreshAccessToken.getAccessToken();
        refresh_token = refreshAccessToken.getRefreshToken();

        Prefs.preferences.edit().putString("access_token", access_token).commit();
        Prefs.preferences.edit().putString("refresh_token", refresh_token).commit();

        Log.e("access_token", access_token);
        Log.e("refresh_token", refresh_token);


        if (code == 200) {
            Toast.makeText(this, getResources().getString(R.string.ready), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_connection_made), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<RefreshAccessToken> call, Throwable t) {
        t.printStackTrace();

        Log.e("onfailure", t.getMessage());
        Toast.makeText(this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItem = menu.findItem(R.id.reload);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.reload) {

            intent = new Intent(LiveStreamsOnAirA.this, LiveStreamsIntentService.class);

            startReload();
            startService(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, StreamsProvider.Streams.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursor = data;
        streamsAdapter.swapCursor(data);

        ComponentName name = new ComponentName(this, Widget.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);

        Intent intent = new Intent(this, Widget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        DatabaseUtils.dumpCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        streamsAdapter.swapCursor(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReloadComplete(Reload event) {
        stopReload();
        Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.reload_complete), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    protected void startReload() {
        menuItem.setActionView(R.layout.progress_bar);
    }

    protected void stopReload() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuItem.setActionView(null);
            }
        }, 2000);
    }
}





































































































































