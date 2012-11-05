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

public class RevisionListAdapter extends CursorAdapter {
    RevisionListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        fillRevisionNumber(view, cursor);
        fillRevisionTimestamp(view, cursor);
        fillRevisionLog(view, cursor);
    }

    private void fillRevisionNumber(View view, Cursor cursor) {
        TextView textView = (TextView)view.findViewById(R.id.revision_number);
        long number = cursor.getLong(cursor.getColumnIndex("revision"));
        textView.setText("" + number);
    }

    private void fillRevisionTimestamp(View view, Cursor cursor) {
        TextView textView = (TextView)view.findViewById(R.id.revision_timestamp);
        long time = cursor.getLong(cursor.getColumnIndex("timestamp"));
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat();
        textView.setText(format.format(date));
    }

    private void fillRevisionLog(View view, Cursor cursor) {
        TextView textView = (TextView)view.findViewById(R.id.revision_log);
        String log = cursor.getString(cursor.getColumnIndex("log"));
        textView.setText(log);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.revision_list, parent, false);
        bindView(view, context, cursor);
        return view;
    }
}
