package com.pb.app.fixchat.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.pb.app.fixchat.data.model.TokenDatabase;

public class DatabaseSQL {

    private static DatabaseSQL instance;

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private ContentValues contentValues;

    private DatabaseSQL(){}

    public TokenDatabase writeTokenFromDB(Context context) {
        dbHelper = new DBHelper(context);
        TokenDatabase token = new TokenDatabase(" ", "0");
        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        }
        contentValues = new ContentValues();
        Cursor cursor = database.query(DBHelper.TABLE_TYPES, null, null, null,
                null, null, null);
        if (cursor.moveToLast()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int tokenIndex = cursor.getColumnIndex(DBHelper.KEY_TOKEN);
            token = new TokenDatabase(cursor.getString(tokenIndex), cursor.getString(idIndex));
        }
        cursor.close();
        Log.d("API", "Данные из DATABASE : " + token.toString());
        return token;
    }

    public void readTokenToDb(Context context, String token){
        dbHelper = new DBHelper(context);
        try {
            database = dbHelper.getReadableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getWritableDatabase();
        }
        contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TOKEN, token);
        database.insert(DBHelper.TABLE_TYPES, null, contentValues);
    }

    public void deleteTokenFromDb(Context context, String id){
        dbHelper = new DBHelper(context);
        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        }
        database.delete(DBHelper.TABLE_TYPES, DBHelper.KEY_ID + "=" + id, null);
    }

    public void deleteEntityFromDb(Context context){
        dbHelper = new DBHelper(context);
        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        }
        database.delete(DBHelper.TABLE_TYPES, null, null);
    }

    public static DatabaseSQL getInstance(){
        if(instance == null) instance = new DatabaseSQL();
        return instance;
    }
}
