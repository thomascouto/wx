package com.tcgr.wx.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.tcgr.wx.R;

/**
 * Adapter para o SearchView.
 * @see SimpleCursorAdapter
 * Created by thomas on 06/04/16.
 */
public class MyCursorAdapter extends SimpleCursorAdapter {

    public MyCursorAdapter(Context context, String[] from, int[] to) {
        super(context, R.layout.search_view_search, null, from, to, android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.search_view_text_view);
        textView.setText(cursor.getString(1));
    }
}