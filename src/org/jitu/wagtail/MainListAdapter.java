package org.jitu.wagtail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MainListAdapter extends CursorAdapter {
    public MainListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setPath(view, cursor);
        setLastModified(view, cursor);
    }
    
    private void setPath(View view, Cursor cursor) {
        TextView path = (TextView)view.findViewById(R.id.path);
        path.setText(cursor.getString(cursor.getColumnIndex("path")));
    }
    
    private void setLastModified(View view, Cursor cursor) {
        TextView lastModified = (TextView)view.findViewById(R.id.last_modified);
        long time = cursor.getLong(cursor.getColumnIndex("lastModified"));
        Date date = new Date(time);
        DateFormat format = SimpleDateFormat.getDateTimeInstance();
        lastModified.setText(format.format(date));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.main_list, parent, false);
        bindView(view, context, cursor);
        return view;
    }
}
