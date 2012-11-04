package org.jitu.wagtail;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final static int ACTIVITY_FILE_CHOOSER = 1;
    private final static int ACTIVITY_LOG_INPUT    = 2;

    private WagtailVC vc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vc = new WagtailVC(this);
        updateList();
    }

    private void updateList() {
        try {
            MainListAdapter adapter = new MainListAdapter(this, vc.getFileCursor());
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
        startActivityForResult(intent, ACTIVITY_FILE_CHOOSER);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode < 0) {
            return;
        }
        switch (requestCode) {
        case ACTIVITY_FILE_CHOOSER:
            onFileChooserResult(data);
            break;
        case ACTIVITY_LOG_INPUT:
            onLogInputResult(data);
            break;
        }
    }

    private void onFileChooserResult(Intent data) {
        String path = data.getStringExtra(FileChooser.RESULT_PATH);
        if (path.isEmpty()) {
            return;
        }
        openLogInput(path);
    }

    private boolean openLogInput(String path) {
        WagtailFile nwf = new WagtailFile(path);
        Intent intent = new Intent(this, LogInput.class);
        intent.putExtra(LogInput.ARG_FILE, nwf);
        startActivityForResult(intent, ACTIVITY_LOG_INPUT);
        return true;
    }

    private void onLogInputResult(Intent data) {
        WagtailFile nwf = (WagtailFile)data.getSerializableExtra(LogInput.RESULT_FILE);
        commitFile(nwf);
    }

    private void commitFile(WagtailFile nwf) {
        vc.commitFile(nwf);
        updateList();
    }
}
