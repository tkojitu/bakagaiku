package org.jitu.wagtail;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity {
    private WagtailSqlHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new WagtailSqlHelper(this);
        updateList();
    }

    private void updateList() {
        try {
            MainListAdapter adapter = new MainListAdapter(this, helper.getFileCursor());
            ListView listView = (ListView)findViewById(R.id.main_list);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_commit:
            return openFileChooser();
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean openFileChooser() {
        Intent intent = new Intent(this, FileChooser.class);
        String root = Environment.getExternalStorageDirectory().getPath();
        intent.putExtra(FileChooser.ARG_ROOT, root);
        startActivityForResult(intent, 0);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = data.getAction();
        if (path.isEmpty()) {
            return;
        }
        helper.insertFile(new File(path), System.currentTimeMillis());
        updateList();
    }
}
