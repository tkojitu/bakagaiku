package org.jitu.wagtail;

import java.io.File;

import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FileSaver extends FileChooser implements View.OnClickListener {
    protected void setupContentView() {
        setContentView(R.layout.file_saver);
        setFileEdit();
        setupButtons();
    }

    protected void setupRoot() {
        File tmp = getArgFile().getParentFile();
        if (tmp.exists() && tmp.isDirectory()) {
            root = currentDir = tmp;
        } else {
            root = currentDir = Environment.getExternalStorageDirectory();
        }
    }

    private void setFileEdit() {
        EditText et = (EditText)findViewById(R.id.file_edit);
        String filename = getArgFile().getName();
        et.setText(filename);
        et.setSelection(et.getText().length());
    }

    protected ListView findFileList() {
        return (ListView)findViewById(R.id.file_list);
    }

    protected FileArrayAdapter newFileArrayAdapter() {
        return FileArrayAdapter.newInstance(FileSaver.this, R.layout.file_chooser_list,
                currentDir);
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

    protected void onFileClick(File file) {
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
