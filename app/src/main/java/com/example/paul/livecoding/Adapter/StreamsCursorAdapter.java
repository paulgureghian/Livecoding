package com.example.paul.livecoding.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paul.livecoding.R;

public class StreamsCursorAdapter extends RecyclerView {

    private Cursor cursor;
    private Context context;

    public StreamsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, (AttributeSet) c);
    }


    public Cursor swapCursor(Cursor newCursor) {

        cursor = newCursor;
        notifyDataSetChanged();
        return newCursor;
    }

    private void notifyDataSetChanged() {
    }
}
