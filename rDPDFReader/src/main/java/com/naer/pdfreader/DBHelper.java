package com.naer.pdfreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private final static int _DBVersion = 1; //<-- 版本
    private final static String _DBName = "SampleList.db";  //<-- db name
    private final static String _TableName = "MySample"; //<-- table name
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_TITLE VARCHAR(50), " +
                "_CONTENT TEXT," +
                "_KIND VARCHAR(10)" +
                ");";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL = "DROP TABLE " + _TableName;
        db.execSQL(SQL);
    }
}
