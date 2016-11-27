package com.example.paul.livecoding.Activities;

import android.app.LoaderManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.paul.livecoding.Adapter.StreamsAdapter;
import com.example.paul.livecoding.DataBase.StreamsProvider;
import com.example.paul.livecoding.R;
import com.example.paul.livecoding.Service.LiveStreamsIntentService;

public class LiveStreamsOnAirA extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Intent intent;
    Context context;
    Boolean isConnected;
    public StreamsAdapter streamsAdapter;
    private static final int CURSOR_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_livestreams);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        this.setTitle(getResources().getString(R.string.live_streams_on_air));

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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, StreamsProvider.Streams.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        streamsAdapter.swapCursor(data);
        DatabaseUtils.dumpCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        streamsAdapter.swapCursor(null);
    }
}






































































































































