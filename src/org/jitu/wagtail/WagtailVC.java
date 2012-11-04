package org.jitu.wagtail;

import android.content.Context;
import android.database.Cursor;

public class WagtailVC {
    private WagtailSqlHelper helper;

    public WagtailVC(Context context) {
        helper = new WagtailSqlHelper(context);
    }

    public Cursor getFileCursor() {
        return helper.getFileCursor();
    }

    public void commitFile(WagtailFile nwf) {
        helper.saveFile(nwf);
    }
}
