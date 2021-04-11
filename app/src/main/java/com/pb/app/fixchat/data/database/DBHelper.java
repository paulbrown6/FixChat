package com.pb.app.fixchat.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "kmsservers";
    public static final String TABLE_TYPES = "servers";

    public static final String KEY_ID = "_id";
    public static final String KEY_TOKEN = "token";

    private static final String TABLE_TYPES_CREATE_SCRIPT = "CREATE TABLE " + TABLE_TYPES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY, " + KEY_TOKEN + " TEXT);";

    private DBQueryManager queryManager;
    private DBUpdateManager updateManager;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        queryManager = new DBQueryManager(getReadableDatabase());
        updateManager = new DBUpdateManager(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_TYPES_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_TYPES);
        onCreate(db);
    }

    public DBQueryManager query() {
        return queryManager;
    }

    public DBUpdateManager update() {
        return updateManager;
    }
}
