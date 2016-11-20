package com.example.paul.livecoding.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.paul.livecoding.Activities.LiveStreamsOnAirA;

public class StreamsCursorAdapter extends RecyclerView.Adapter<StreamsCursorAdapter.StreamsAdapterViewHolder> {

    public StreamsCursorAdapter(LiveStreamsOnAirA liveStreamsOnAirA, Object o) {
    }

    @Override
    public StreamsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StreamsAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StreamsAdapterViewHolder extends RecyclerView.ViewHolder {
        public StreamsAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}

