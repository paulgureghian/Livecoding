package com.example.paul.livecoding.Activities;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paul.livecoding.Adapter.StreamsAdapter;
import com.example.paul.livecoding.DataBase.StreamsColumns;
import com.example.paul.livecoding.DataBase.StreamsProvider;
import com.example.paul.livecoding.R;
import com.example.paul.livecoding.RecyclerViewListener.RecyclerViewItemClickListener;
import com.example.paul.livecoding.Service.LiveStreamsIntentService;
import com.example.paul.livecoding.Widget.Widget;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LiveStreamsOnAirA extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Intent intent;
    Cursor mCursor;
    Context context;
    Boolean isConnected;
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

                            context.startActivity(intent);
                        }
                    }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.reload_icon_layout);
        menu.findItem(R.id.reload_icon).setActionView(imageView);

        Animation rotation = AnimationUtils.loadAnimation(this, R.xml.rotation);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.reload) {

            intent = new Intent(LiveStreamsOnAirA.this, LiveStreamsIntentService.class);

            intent.putExtra()
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
}






































































































































