package com.pb.app.fixchat.data.database;

import android.database.sqlite.SQLiteDatabase;

public class DBUpdateManager {

    private SQLiteDatabase database;

    DBUpdateManager (SQLiteDatabase database){
        this.database = database;
    }

//    private void update(String column, int key, String value){
//        ContentValues cv = new ContentValues();
//        cv.put(column, value);
//        database.update(DBHelper.TABLE_MOVIE, cv, DBHelper.MOVIE_ID + " - " + key, null);
//    }
//
//    private void update(String column, int key, long value){
//        ContentValues cv = new ContentValues();
//        cv.put(column, value);
//        database.update(DBHelper.TABLE_MOVIE, cv, DBHelper.MOVIE_ID + " - " + key, null);
//    }
}
