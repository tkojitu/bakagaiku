package org.jitu.wagtail;

import android.content.Context;
import android.database.Cursor;

public class CheckOutControl {
    private WagtailSqlHelper helper;

    public CheckOutControl(Context context) {
        helper = new WagtailSqlHelper(context);
    }

    public Cursor getRevisionCursor(long id) {
        return helper.getRevisionCursor(id);
    }
}
