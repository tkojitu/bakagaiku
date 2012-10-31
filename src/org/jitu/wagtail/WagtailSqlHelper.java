package org.jitu.wagtail;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "_id integer, " +
                "revision integer, " +
                "content blob, " +
                "foreign key (_id) references WagtailFiles(_id), " +
                "foreign key (revision) references WagtailRevisions(revision)" +
                ")";
        db.execSQL(sql);
    }

    private void createWagtailRevisions(SQLiteDatabase db) {
        String sql = "create table WagtailRevisions (" +
                "_id integer, " +
                "revision integer, " +
                "timestamp integer, " +
                "log text, " +
                "foreign key (_id) references WagtailFiles(_id)" +
                ")";
        db.execSQL(sql);
    }

    private void createWagtailFiles(SQLiteDatabase db) {
        String sql = "create table WagtailFiles (" +
                "_id integer primary key autoincrement, " +
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
    
    public long insertFile(File file, long lastModified) {
        ContentValues values = new ContentValues();
        values.put("path", file.getAbsolutePath());
        values.put("lastModified", lastModified);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert("WagtailFiles", null, values);
    }

    public Cursor getFileCursor() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("WagtailFiles", new String[]{"_id", "path", "lastModified"},
                null, null, null, null, null);
    }
}
