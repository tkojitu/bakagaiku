package org.jitu.wagtail;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WagtailSqlHelper extends SQLiteOpenHelper {
    private static final String NAME = "wagtailvc.db";
    private static final int VERSION = 1;

    public WagtailSqlHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createWagtailFiles(db);
        createWagtailRevisions(db);
        createWagtailContents(db);
    }

    private void createWagtailContents(SQLiteDatabase db) {
        String sql = "create table WagtailContents (" +
                "_id integer," +
                "revision integer," +
                "content blob," +
                "foreign key (_id) references WagtailFiles(_id)" +
                ")";
        db.execSQL(sql);
    }

    private void createWagtailRevisions(SQLiteDatabase db) {
        String sql = "create table WagtailRevisions (" +
                "_id integer," +
                "revision integer," +
                "timestamp integer," +
                "log text," +
                "foreign key (_id) references WagtailFiles(_id)" +
                ")";
        db.execSQL(sql);
    }

    private void createWagtailFiles(SQLiteDatabase db) {
        String sql = "create table WagtailFiles (" +
                "_id integer primary key autoincrement," +
                "path text not null," +
                "lastModified integer not null" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropWagtailContents(db);
        dropWagtailRevisions(db);
        dropWagtailFiles(db);
        onCreate(db);
    }

    private void dropWagtailFiles(SQLiteDatabase db) {
        db.execSQL(getSqlDropWagtailFiles());
    }

    private String getSqlDropWagtailFiles() {
        return getSqlDropTable("WagtailFiles");
    }

    private void dropWagtailRevisions(SQLiteDatabase db) {
        db.execSQL(getSqlDropWagtailRevisions());
    }

    private String getSqlDropWagtailRevisions() {
        return getSqlDropTable("WagtailRevisions");
    }

    private void dropWagtailContents(SQLiteDatabase db) {
        db.execSQL(getSqlDropWagtailContents());
    }

    private String getSqlDropWagtailContents() {
        return getSqlDropTable("WagtailContents");
    }
    
    private String getSqlDropTable(String table) {
        return "drop table if exists " + table;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public Cursor getFileCursor() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("WagtailFiles", new String[]{"_id", "path", "lastModified"},
                null, null, null, null, null);
    }

    public void saveFile(WagtailFile nwf) {
        WagtailFile found = findLatestFile(nwf.getFile());
        if (found == null) {
            insertFile(nwf);
        } else {
            updateFile(found, nwf);
        }
    }

    private WagtailFile findLatestFile(File file) {
        WagtailFile result = findByFileFromFiles(file);
        if (result == null) {
            return null;
        }
        WagtailRevision rev = findLatestRevision(result.getId());
        result.setRevision(rev);
        return result;
    }

    private WagtailFile findByFileFromFiles(File file) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("WagtailFiles",
                new String[]{"_id", "path", "lastModified"},
                "path=?",
                new String[]{file.getAbsolutePath()},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        long id = cursor.getLong(cursor.getColumnIndex("_id"));
        String path = cursor.getString(cursor.getColumnIndex("path"));
        long lastModified = cursor.getLong(cursor.getColumnIndex("lastModified"));
        return new WagtailFile(id, path, lastModified);
    }

    private WagtailRevision findLatestRevision(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("WagtailRevisions",
                new String[]{"_id", "max(revision) as revision", "timestamp", "log"},
                "_id=?",
                new String[]{"" + id},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        long revision = cursor.getLong(cursor.getColumnIndex("revision"));
        long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
        String log = cursor.getString(cursor.getColumnIndex("log"));
        return new WagtailRevision(id, revision, timestamp, log);
    }

    private void insertFile(WagtailFile nwf) {
        long now = System.currentTimeMillis();
        nwf.setLastModified(now);
        nwf.setRevisionTimestamp(now);
        insertFileToFiles(nwf);
        insertRevision(nwf);
        insertContent(nwf);
    }

    private void insertFileToFiles(WagtailFile nwf) {
        ContentValues values = new ContentValues();
        values.put("path", nwf.getAbsolutePath());
        values.put("lastModified", nwf.getLastModified());
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insertOrThrow("WagtailFiles", null, values);
        nwf.setId(id);
    }

    private void insertRevision(WagtailFile nwf) {
        ContentValues values = new ContentValues();
        values.put("_id", nwf.getId());
        values.put("revision", nwf.getRevisionNumber());
        values.put("timestamp", nwf.getRevisionTimestamp());
        values.put("log", nwf.getRevisionLog());
        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow("WagtailRevisions", null, values);
    }

    private void updateFile(WagtailFile found, WagtailFile nwf) {
        long now = System.currentTimeMillis();
        nwf.setId(found.getId());
        nwf.setLastModified(now);
        nwf.setRevisionTimestamp(now);
        nwf.setRevisionNumber(found.getRevisionNumber() + 1);
        updateFileInFiles(nwf);
        insertRevision(nwf);
        insertContent(nwf);
    }

    private void updateFileInFiles(WagtailFile wf) {
        ContentValues values = new ContentValues();
        values.put("lastModified", wf.getLastModified());
        SQLiteDatabase db = getWritableDatabase();
        db.update("WagtailFiles", values, "_id like " + wf.getId(), null);
    }

    private void insertContent(WagtailFile wf) {
        ContentValues values = new ContentValues();
        values.put("_id", wf.getId());
        values.put("revision", wf.getRevisionNumber());
        values.put("content", wf.getRevisionBytes());
        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow("WagtailContents", null, values);
    }

    public long findId(String path) {
        WagtailFile wf = findByFileFromFiles(new File(path));
        return wf.getId();
    }

    public Cursor getRevisionCursor(long id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("WagtailRevisions",
                new String[]{"_id", "revision", "timestamp", "log"},
                "_id=?",
                new String[]{"" + id},
                null, null, null);
    }

    public void findContent(WagtailFile wf) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("WagtailContents",
                new String[]{"_id", "revision", "content"},
                "_id=? and revision=?",
                new String[]{"" + wf.getId(), "" + wf.getRevisionNumber()},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return;
        }
        byte[] content = cursor.getBlob(cursor.getColumnIndex("content"));
        wf.setRevisionBytes(content);
    }
}
