package org.jitu.wagtail;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class RevisionActivity extends ListActivity {
    public static final String ARG_ID = "ARG_ID";
    public static final String ARG_FILE = "ARG_FILE";

    private CheckOutControl control;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        control = new CheckOutControl(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra(ARG_FILE);
        setTitle(title);
        long id = intent.getLongExtra(ARG_ID, -1);
        fill(id);
    }

    private void fill(long id) {
        Cursor cursor = control.getRevisionCursor(id);
        RevisionListAdapter adapter = new RevisionListAdapter(this, cursor);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        TextView textView = (TextView)view.findViewById(R.id.revision_number);
        long number = Long.parseLong(textView.getText().toString());
        long fileId = getIntent().getLongExtra(ARG_ID, -1);
        String path = getIntent().getStringExtra(ARG_FILE);
        WagtailFile wf = WagtailFile.newToCheckOut(fileId, path, number);
        control.checkOut(wf);
        finish();
    }

    private void backToParent(int resultCode) {
        setResult(resultCode);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
             return cancel();
        default:
            return true;
        }
    }

    private boolean cancel() {
        backToParent(-1);
        return true;
    }

    @Override
    public void onBackPressed () {
        cancel();
    }
}
