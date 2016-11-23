package com.example.paul.livecoding.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.paul.livecoding.R;

public class StreamsAdapter extends RecyclerView.Adapter {
    private Cursor mCursor;
    private Context context;

    private static class StreamsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        StreamsViewHolder(View view) {

            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    @Override
    public StreamsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new StreamsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
