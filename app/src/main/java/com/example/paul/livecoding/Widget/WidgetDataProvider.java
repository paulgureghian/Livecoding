package com.example.paul.livecoding.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.paul.livecoding.DataBase.StreamsColumns;
import com.example.paul.livecoding.DataBase.StreamsProvider;
import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;
import com.example.paul.livecoding.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    StreamsProvider.Streams streams = new StreamsProvider.Streams();
    private List<LiveStreamsOnAirP> collection = new ArrayList<>();
    private Context context;
    private Intent intent;
    private Cursor mCursor;

    private void initData() {
        collection.clear();

        mCursor = context.getContentResolver().query(StreamsProvider.Streams.CONTENT_URI, null, null, null, null);

        DatabaseUtils.dumpCursor(mCursor);

        int index = mCursor.getColumnIndex(StreamsColumns.THUMBNAIL_URL);

        if (mCursor != null) {
            while (mCursor.moveToNext()) {

                LiveStreamsOnAirP liveStreamsOnAirP = new LiveStreamsOnAirP();
                liveStreamsOnAirP.setThumbnailUrl(mCursor.getString(mCursor.getColumnIndex(StreamsColumns.THUMBNAIL_URL)));
                collection.add(liveStreamsOnAirP);
            }
        } else {
        }
        mCursor.close();
    }

    WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        remoteView.setTextColor(R.layout.widget_layout, Color.BLACK);

        int columnIndex;
        columnIndex = mCursor.getColumnIndex(StreamsColumns.THUMBNAIL_URL);

        Bitmap bitmap = null;
        try {
            try {
                bitmap = Glide.with(context).load(mCursor.getString(columnIndex)).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                Log.e("widget_data_provider", String.valueOf(bitmap));

            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        remoteView.setImageViewBitmap(R.id.thumbnail, bitmap);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
