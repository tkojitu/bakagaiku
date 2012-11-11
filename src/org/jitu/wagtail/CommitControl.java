package org.jitu.wagtail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.Toast;

public class CommitControl {
    private Context context;
    private WagtailSqlHelper helper;

    public CommitControl(Context context) {
        this.context = context;
        helper = new WagtailSqlHelper(context);
    }

    public Cursor getFileCursor() {
        try {
            return helper.getFileCursor();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void commitFile(WagtailFile nwf) {
        byte[] bytes = readFile(nwf);
        if (bytes == null) {
            return;
        }
        nwf.setRevisionBytes(bytes);
        try {
            helper.saveFile(nwf);
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private byte[] readFile(WagtailFile nwf) {
        try {
            URL url = nwf.getFile().toURI().toURL();
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            int nread;
            byte[] chunk = new byte[8192];
            ByteArrayBuffer buf = new ByteArrayBuffer(8192);
            while ((nread = bis.read(chunk, 0, chunk.length)) != -1) {
                buf.append(chunk, 0, nread);
            }
            return buf.toByteArray();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public long findId(String path) {
        try {
            return helper.findId(path);
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }
}
