package com.example.paul.livecoding.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.paul.livecoding.database.StreamsColumns;
import com.example.paul.livecoding.R;

public class StreamsAdapter extends RecyclerView.Adapter<StreamsAdapter.StreamsViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public StreamsAdapter(Context context) {
        mContext = context;
    }

    static class StreamsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView user_slug;
        TextView title;
        TextView is_live;
        TextView coding_category;

        StreamsViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageView);
            user_slug = (TextView) view.findViewById(R.id.user_slug);
            title = (TextView) view.findViewById(R.id.title);
            is_live = (TextView) view.findViewById(R.id.is_live);
            coding_category = (TextView) view.findViewById(R.id.coding_category);
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

        int columnIndex;

        columnIndex = mCursor.getColumnIndex(StreamsColumns.USER_SLUG);
        holder.user_slug.setText(mCursor.getString(columnIndex));

        columnIndex = mCursor.getColumnIndex(StreamsColumns.TITLE);
        holder.title.setText(mCursor.getString(columnIndex));

        columnIndex = mCursor.getColumnIndex(StreamsColumns.CODING_CATEGORY);
        holder.coding_category.setText(mCursor.getString(columnIndex));

        columnIndex = mCursor.getColumnIndex(StreamsColumns.THUMBNAIL_URL);
        Glide.with(mContext).load(mCursor.getString(columnIndex)).into(holder.imageView);
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

    public Cursor getCursor() {
        return mCursor;
    }
}