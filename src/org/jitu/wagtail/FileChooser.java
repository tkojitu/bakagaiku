package org.jitu.wagtail;

import java.io.File;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;

public class FileChooser extends Activity implements OnItemClickListener {
    public static final String ARG_ROOT = "ARG_ROOT";
    public static final String RESULT_PATH = "path";

    private File root;
    private File currentDir;
    private FileArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.file_chooser);
        setupList();
        setTitleDir();
    }

    private void setupList() {
        Intent intent = getIntent();
        String path = intent.getStringExtra(ARG_ROOT);
        root = currentDir = new File(path);
        setupList(currentDir);
    }

    private void setupList(File dir) {
        ListView lv = (ListView)findViewById(R.id.file_chooser_list);
        lv.setOnItemClickListener(this);
        adapter = FileArrayAdapter.newInstance(FileChooser.this, R.layout.file_chooser_list, dir);
        lv.setAdapter(adapter);
    }

    private void setTitleDir() {
        setTitle(currentDir.getName());
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File save = currentDir;
        try {
            File file = adapter.getFile(position);
            if (file.isDirectory()) {
                currentDir = file.getAbsoluteFile();
                setupList(currentDir);
            } else {
                onFileClick(file);
            }
        } catch (Exception e) {
            currentDir = save;
        }
        setTitleDir();
    }

    private void onFileClick(File file) {
        backToParent(0, file.getAbsolutePath());
    }
    
    private void backToParent(int resultCode, String path) {
        Intent intent = new Intent();
        intent.putExtra("path", path);
        setResult(resultCode, intent);
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
        backToParent(-1, "");
        return true;
    }

    @Override
    public void onBackPressed () {
        if (currentDir.equals(root)) {
            cancel();
            return;
        }
        moveUp();
    }

    private void moveUp() {
        currentDir = currentDir.getParentFile();
        setupList(currentDir);
        setTitleDir();
    }
}
