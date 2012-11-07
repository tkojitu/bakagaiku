package org.jitu.wagtail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.app.ListActivity;
import android.content.Intent;

public class FileChooser extends ListActivity {
    public static final String ARG_ROOT = "ARG_ROOT";
    public static final String RESULT_PATH = "path";

    private File root;
    private File currentDir;
    private FileArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String path = intent.getStringExtra(ARG_ROOT);
        root = currentDir = new File(path);
        fill(currentDir);
        setTitle(currentDir.getName());
    }

    private void fill(File root) {
        List<File> files = new ArrayList<File>();
        File[] array = root.listFiles();
        for (File file: array) {
            files.add(file);
        }
        Collections.sort(files);
        if (!root.getName().equalsIgnoreCase("sdcard")) {
            files.add(0, root.getParentFile());
        }
        adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_chooser, files);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        File save = currentDir;
        try {
            File file = adapter.getFile(position);
            if (file.isDirectory()) {
                currentDir = file.getAbsoluteFile();
                fill(currentDir);
            } else {
                onFileClick(file);
            }
        } catch (Exception e) {
            currentDir = save;
        }
        setTitle(currentDir.getName());
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
        fill(currentDir);
        setTitle(currentDir.getName());
    }
}
