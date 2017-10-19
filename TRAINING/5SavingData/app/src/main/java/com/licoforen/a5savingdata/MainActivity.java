package com.licoforen.a5savingdata;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /********* SharedPreferences *********/
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();

        // saving highscore
        int newHighScore = 10;
        prefEditor.putInt(getString(R.string.pref_high_score), newHighScore);
        prefEditor.apply();

        // getting highscore
        int defaultHighScore = getResources().getInteger(R.integer.high_score_default);
        int highScore = sharedPref.getInt(getString(R.string.pref_high_score), defaultHighScore);


        TextView txtHighScore = (TextView) findViewById(R.id.txtHighScore);
        txtHighScore.setText(Integer.toString(highScore));
        /************************************/



        /************** Files ***************/
        // permission in manifest

        // internal storage
        String filename = "internalFile.txt";
        String message = "Hello world";

        try {
            FileOutputStream fout = openFileOutput(filename, MODE_PRIVATE);
            fout.write(message.getBytes());
            fout.close();

            // temp file
            File file = File.createTempFile(filename, null, getCacheDir());
            deleteFile(filename);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // external storage
        File privateExternalDir = getExternalFilesDir(null);
        File publicExternalPicturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        privateExternalDir.getFreeSpace();
        publicExternalPicturesDir.getTotalSpace();
        /************************************/



        /*********** SQL database ***********/
        SqlDbHelper mDbHelper = new SqlDbHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // *********** INSERTING
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SqlDbContract.SqlDb.COLUMN_NAME_ID, 1);
        values.put(SqlDbContract.SqlDb.COLUMN_NAME_TITLE, "'Example title'");
        //values.put(SqlDbContract.SqlDb.COLUMN_NAME_SUBTITLE, "'Example sub'");

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                SqlDbContract.SqlDb.TABLE_NAME,
                null,
                values);



        // *********** QUERYING
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SqlDbContract.SqlDb._ID,
                SqlDbContract.SqlDb.COLUMN_NAME_TITLE,
                //SqlDbContract.SqlDb.COLUMN_NAME_SUBTITLE,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                SqlDbContract.SqlDb.COLUMN_NAME_TITLE + " DESC";

        Cursor c = db.query(
                SqlDbContract.SqlDb.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(SqlDbContract.SqlDb._ID)
        );
        c.close();


        // *********** UPDATING
        // New value for one column
        ContentValues values2 = new ContentValues();
        values2.put(SqlDbContract.SqlDb.COLUMN_NAME_TITLE, "Example title update");

        // Which row to update, based on the ID
        String selection2 = SqlDbContract.SqlDb.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs2 = { "0" };

        int count = db.update(
                SqlDbContract.SqlDb.TABLE_NAME,
                values2,
                selection2,
                selectionArgs2);


        // *********** DELETING
        // Define 'where' part of query.
        String selection = SqlDbContract.SqlDb.COLUMN_NAME_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "0" };
        // Issue SQL statement.
        db.delete(SqlDbContract.SqlDb.TABLE_NAME, selection, selectionArgs);
        /************************************/
    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(null, "Directory not created");
        }
        return file;
    }
}
