package org.jitu.wagtail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LogInputActivity extends Activity {
    public final static String ARG_FILE = "ARG_FILE";
    public final static String RESULT_FILE = "RESULT_FILE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_input);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        updateTitle();
    }

    public void updateTitle() {
        Intent intent = getIntent();
        WagtailFile file = (WagtailFile)intent.getSerializableExtra(ARG_FILE);
        setTitle(file.getAbsolutePath());
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

    private void backToParent(int resultCode, String log) {
        Intent intent = getIntent();
        WagtailFile file = (WagtailFile)intent.getSerializableExtra(ARG_FILE);
        file.setRevisionLog(log);
        Intent newIntent = new Intent();
        newIntent.putExtra(RESULT_FILE, file);
        setResult(resultCode, newIntent);
        finish();
    }

    public void onClick(View view) {
        EditText edit = (EditText)findViewById(R.id.log);
        String str = edit.getText().toString();
        backToParent(0, str);
    }

    @Override
    public void onBackPressed () {
        cancel();
    }
}
