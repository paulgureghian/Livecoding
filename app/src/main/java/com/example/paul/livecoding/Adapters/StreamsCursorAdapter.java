package com.example.paul.livecoding.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.paul.livecoding.Activities.LiveStreamsOnAirA;

public class StreamsCursorAdapter extends CursorRecyclerViewAdapter<StreamsCursorAdapter.ViewHolder> {


    public StreamsCursorAdapter(LiveStreamsOnAirA liveStreamsOnAirA, Object o) {
        super();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
