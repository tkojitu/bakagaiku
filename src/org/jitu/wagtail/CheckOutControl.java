package org.jitu.wagtail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CheckOutControl {
    private WagtailSqlHelper helper;

    public CheckOutControl(Context context) {
        helper = new WagtailSqlHelper(context);
    }

    public Cursor getRevisionCursor(long id) {
        return helper.getRevisionCursor(id);
    }

    public void checkOut(WagtailFile wf) {
        helper.findContent(wf);
        saveFile(wf);
    }

    private void saveFile(WagtailFile wf) {
        File file = wf.getFile();
        long number = wf.getRevisionNumber();
        String path = file.getAbsolutePath() + number;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(wf.getRevisionBytes());
            bos.close();
        } catch (IOException e) {
            Log.e("CheckOutControl", e.getMessage());
        }
    }
}
