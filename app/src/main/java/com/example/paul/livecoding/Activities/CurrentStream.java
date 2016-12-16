package com.example.paul.livecoding.Activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.example.paul.livecoding.Adapter.StreamsAdapter;
import com.example.paul.livecoding.DataBase.StreamsColumns;
import com.example.paul.livecoding.DataBase.StreamsProvider;
import com.example.paul.livecoding.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class CurrentStream extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnPreparedListener {

    int columnsId;
    Intent intent;
    Cursor mCursor;
    Context context;
    EMVideoView emVideoView;
    public StreamsAdapter streamsAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int CURSOR_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_stream);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        context = this;
        emVideoView = (EMVideoView) findViewById(R.id.video_view);
        intent = getIntent();
        columnsId = intent.getExtras().getInt(StreamsColumns._ID);
        Log.e("ID", String.valueOf(columnsId));

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        streamsAdapter = new StreamsAdapter(this);
    }

    @Override
    public void onPrepared() {

        emVideoView.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, StreamsProvider.Streams.withId(columnsId),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (mCursor != null && mCursor.moveToFirst()) {
            mCursor = data;
            mCursor.moveToFirst();
            streamsAdapter.swapCursor(data);

            int columnIndex;
            columnIndex = mCursor.getColumnIndex(StreamsColumns.VIEWING_URLS2);
            String stream_url = mCursor.getString(columnIndex);

            emVideoView.setOnPreparedListener(this);
            emVideoView.setVideoURI(Uri.parse(stream_url));

            DatabaseUtils.dumpCursor(data);
            Log.e("cursor", mCursor.getCount() + "");

            String name = mCursor.getString(mCursor.getColumnIndex(StreamsColumns.TITLE));
            String category = mCursor.getString(mCursor.getColumnIndex(StreamsColumns.CODING_CATEGORY));
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

            mCursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        streamsAdapter.swapCursor(null);
    }
}



