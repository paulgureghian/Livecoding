package com.example.paul.livecoding.Activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.paul.livecoding.Adapter.StreamsAdapter;
import com.example.paul.livecoding.DataBase.StreamsProvider;
import com.example.paul.livecoding.R;

public class CurrentStream extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    Cursor mCursor;
    Context context;
    public StreamsAdapter streamsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_stream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        streamsAdapter = new StreamsAdapter(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, StreamsProvider.withId(id),
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
