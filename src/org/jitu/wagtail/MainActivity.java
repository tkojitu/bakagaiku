package org.jitu.wagtail;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        String root = data.getAction();
        setTitle(root);
    }
}
