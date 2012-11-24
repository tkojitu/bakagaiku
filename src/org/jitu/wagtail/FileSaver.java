package org.jitu.wagtail;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FileSaver extends Activity implements OnItemClickListener, View.OnClickListener {
    public static final String ARG_PATH = "ARG_PATH";
    public static final String RESULT_PATH = "RESULT_PATH";

    private File root;
    private File currentDir;
    private FileArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.file_saver);
        setupRoot();
        setupList(currentDir);
        setFileEdit();
        setupButtons();
        setTitleDir();
    }

    private void setupRoot() {
        File tmp = getArgFile().getParentFile();
        if (tmp.exists() && tmp.isDirectory()) {
            root = currentDir = tmp;
        } else {
            root = currentDir = Environment.getExternalStorageDirectory();
        }
    }

    private File getArgFile() {
        String path = getIntent().getStringExtra(ARG_PATH);
        return new File(path);
    }

    private void setFileEdit() {
        EditText et = (EditText)findViewById(R.id.file_edit);
        String filename = getArgFile().getName();
        et.setText(filename);
        et.setSelection(et.getText().length());
    }

    private void setupList(File dir) {
        ListView lv = (ListView)findViewById(R.id.file_list);
        lv.setOnItemClickListener(this);
        adapter = FileArrayAdapter.newInstance(FileSaver.this, R.layout.file_chooser_list, dir);
        lv.setAdapter(adapter);
    }

    private void setTitleDir() {
        setTitle(currentDir.getName());
    }

    private void setupButtons() {
        setupOkButton();
        setupCancelButton();
    }

    private void setupOkButton() {
        Button b = (Button)findViewById(R.id.ok);
        b.setOnClickListener(this);
    }

    private void setupCancelButton() {
        Button b = (Button)findViewById(R.id.cancel);
        b.setOnClickListener(this);
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
    }

    private void backToParent(int resultCode, String path) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_PATH, path);
        setResult(resultCode, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
             return cancelToHome();
        default:
            return true;
        }
    }

    private boolean cancelToHome() {
        backToParent(-2, "");
        return true;
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

    public void onClick(View v) {
        if (v == findViewById(R.id.ok)) {
            onOk();
        } else {
            cancel();
        }
    }

    private void onOk() {
        String path = getSavedPath();
        backToParent(0, path);
    }

    private String getSavedPath() {
        EditText et = (EditText)findViewById(R.id.file_edit);
        String text = et.getText().toString();
        return currentDir + File.separator + text;
    }
}
