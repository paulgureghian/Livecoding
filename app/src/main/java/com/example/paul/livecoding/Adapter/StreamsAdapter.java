package com.example.paul.livecoding.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class StreamsAdapter extends RecyclerView.Adapter {

    private Cursor cursor;
    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public Cursor swapCursor(Cursor newCusor) {
        cursor = newCusor;
        notifyDataSetChanged();
        return newCusor;
    }
}