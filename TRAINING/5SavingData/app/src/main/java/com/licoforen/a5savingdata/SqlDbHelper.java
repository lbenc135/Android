package com.licoforen.a5savingdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lbenc on 6.8.2016..
 */

public class SqlDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MySqlDb";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SqlDbContract.SqlDb.TABLE_NAME + " (" +
                    SqlDbContract.SqlDb._ID + " INTEGER PRIMARY KEY," +
                    SqlDbContract.SqlDb.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    SqlDbContract.SqlDb.COLUMN_NAME_TITLE + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SqlDbContract.SqlDb.TABLE_NAME;

    public SqlDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
