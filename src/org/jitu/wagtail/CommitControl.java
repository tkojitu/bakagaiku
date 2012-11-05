package org.jitu.wagtail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.database.Cursor;

public class CommitControl {
    private WagtailSqlHelper helper;

    public CommitControl(Context context) {
        helper = new WagtailSqlHelper(context);
    }

    public Cursor getFileCursor() {
        return helper.getFileCursor();
    }

    public void commitFile(WagtailFile nwf) {
        byte[] bytes = readFile(nwf);
        nwf.setRevisionBytes(bytes);
        helper.saveFile(nwf);
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
            return null;
        }
    }
}
