package com.example.paul.livecoding.Activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.paul.livecoding.Adapter.StreamsAdapter;
import com.example.paul.livecoding.DataBase.StreamsColumns;
import com.example.paul.livecoding.DataBase.StreamsProvider;
import com.example.paul.livecoding.R;

public class CurrentStream extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    int id;
    Intent intent;
    Cursor mCursor;
    Context context;
    public StreamsAdapter streamsAdapter;
    private static final int CURSOR_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_stream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        id = intent.getExtras().getInt(StreamsColumns._ID);
        Log.e("ID", String.valueOf(id));

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        streamsAdapter = new StreamsAdapter(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, StreamsProvider.Streams.withId(id),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursor = data;
        streamsAdapter.swapCursor(data);
        DatabaseUtils.dumpCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        streamsAdapter.swapCursor(null);
    }
}
