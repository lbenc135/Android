package com.licoforen.sqltest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDB.db";
    public static final String TABLE_NAME = "MyTable";
    public static final String COLUMNS[] = {"id", "name", "phone", "email", "street", "place"};
    public static final String COLUMNSTYPE[] = {" INTEGER PRIMARY KEY", " TEXT", " TEXT", " TEXT", " TEXT", " TEXT"};

    public static final int numOfCols = 6;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command = "CREATE TABLE " + TABLE_NAME + " (";
        for(int i=0;i<numOfCols;i++) {
            command += COLUMNS[i] + COLUMNSTYPE[i];
            if(i<numOfCols-1) command += ", ";
            else command += ')';
        }
        db.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String args[])
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i=1;i<numOfCols;i++) {
            contentValues.put(COLUMNS[i], args[i-1]);
        }
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String columns[], String where, String[] whereArgs, String order){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(TABLE_NAME, columns, where, whereArgs, null, null, order);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update(Integer id, String args[])
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int i=1;i<numOfCols;i++) {
            contentValues.put(COLUMNS[i], args[i-1]);
        }
        db.update(TABLE_NAME, contentValues,
                COLUMNS[0] + " = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public Integer delete(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMNS[0] + " = ? ",
                new String[] { Integer.toString(id) });
    }


    public ArrayList<String> getArray(String columns[], String where, String[] whereArgs, String order, String column)
    {
        Cursor cur = getData(columns, where, whereArgs, order);
        cur.moveToFirst();

        ArrayList<String> array_list = new ArrayList<>();
        while(!cur.isAfterLast()){
            array_list.add(cur.getString(cur.getColumnIndex(column)));
            cur.moveToNext();
        }
        cur.close();

        return array_list;
    }
}