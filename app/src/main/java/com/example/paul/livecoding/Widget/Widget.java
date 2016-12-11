package com.example.paul.livecoding.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.paul.livecoding.Adapter.StreamsAdapter;
import com.example.paul.livecoding.DataBase.StreamsColumns;
import com.example.paul.livecoding.R;

public class Widget extends AppWidgetProvider {

    Cursor cursor;
    Context context;
    public StreamsAdapter streamsAdapter = new StreamsAdapter(context);

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; ++i) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);

            Intent intent = new Intent(context, WidgetService.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

            for (int appWidgetId : appWidgetIds) {

                updateAppWidget(context, appWidgetManager, appWidgetId);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

                Intent intent1 = new Intent(context, WidgetDataProvider.class);
                cursor = streamsAdapter.getCursor();
                int id = cursor.getInt(cursor.getColumnIndex(StreamsColumns._ID));
                intent1.putExtra(StreamsColumns._ID, id);
                context.startActivity(intent1);
            }
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, WidgetService.class));
    }

    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, WidgetService.class));
    }
}

