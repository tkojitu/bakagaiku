package org.jitu.wagtail;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainListAdapter extends CursorAdapter {
    public MainListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        setPath(view, cursor);
        setLastModified(view, cursor);
    }
    
    private void setPath(View view, Cursor cursor) {
        TextView path = (TextView)view.findViewById(R.id.path);
        path.setText(cursor.getString(cursor.getColumnIndex("path")));
    }
    
    private void setLastModified(View view, Cursor cursor) {
        TextView lastModified = (TextView)view.findViewById(R.id.lastModified);
        long time = cursor.getLong(cursor.getColumnIndex("lastModified"));
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat();
        lastModified.setText(format.format(date));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.main_list, parent, false);
        bindView(view, context, cursor);
        return view;
    }
}
