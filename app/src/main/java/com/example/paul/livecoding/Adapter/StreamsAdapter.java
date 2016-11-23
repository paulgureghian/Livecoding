package com.example.paul.livecoding.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.paul.livecoding.R;
import com.example.paul.livecoding.Service.LiveStreamsIntentService;

public class StreamsAdapter extends RecyclerView.Adapter<StreamsAdapter.StreamsViewHolder> {
    private Cursor mCursor;
    private Context mContext;

    static class StreamsViewHolder extends RecyclerView.ViewHolder {

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
        Log.e("itemView", String.valueOf(itemView));
        return new StreamsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StreamsViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        int columnId = mCursor.getInt(LiveStreamsIntentService.COL_URL);



    }


    @Override
    public int getItemCount() {
        if (mCursor != null) {
            Log.e("getItemCount", String.valueOf(mCursor));
            return mCursor.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
