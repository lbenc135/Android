package com.licoforen.a9sharingfiles;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSelectActivity extends AppCompatActivity {

    // The path to the root of this app's internal storage
    private File mPrivateRootDir;
    // The path to the "images" subdirectory
    private File mImagesDir;
    // Array of files in the images subdirectory
    File[] mImageFiles;
    // Array of filenames corresponding to mImageFiles
    String[] mImageFilenames;
    Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);


        // Set up an Intent to send back to apps that request a file
        final Intent mResultIntent =
                new Intent("com.example.myapp.ACTION_RETURN_FILE");
        // Get the files/ subdirectory of internal storage
        mPrivateRootDir = getFilesDir();
        // Get the files/images subdirectory;
        mImagesDir = new File(mPrivateRootDir, "images");
        // Get the files in the images subdirectory
        mImageFiles = mImagesDir.listFiles();
        // Set the Activity's result to null to begin with
        setResult(Activity.RESULT_CANCELED, null);
        /*
         * Display the file names in the ListView mFileListView.
         * Back the ListView with the array mImageFilenames, which
         * you can create by iterating through mImageFiles and
         * calling File.getAbsolutePath() for each File
         */

        ListView mFileListView = (ListView) findViewById(R.id.listView1);
        final ArrayList<String> mImageFilenames = new ArrayList<>();
        for(File imageFile : mImageFiles) {
            mImageFilenames.add(imageFile.getAbsolutePath());
        }
        ListAdapter mFileListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mImageFilenames);
        mFileListView.setAdapter(mFileListViewAdapter);

        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                File requestFile = new File(mImageFilenames.get(position));

                // Use the FileProvider to get a content URI
                try {
                    fileUri = FileProvider.getUriForFile(
                            getApplication(),
                            "com.example.myapp.fileprovider",
                            requestFile);
                } catch (IllegalArgumentException e) {
                    Log.e("File Selector",
                            "The selected file can't be shared: " +
                                    mImageFilenames.get(position));
                }

                if (fileUri != null) {
                    // Grant temporary read permission to the content URI
                    mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Put the Uri and MIME type in the result Intent
                    mResultIntent.setDataAndType(fileUri,
                            getContentResolver().getType(fileUri));
                    // Set the result
                    setResult(Activity.RESULT_OK, mResultIntent);
                } else {
                    mResultIntent.setDataAndType(null, "");
                    setResult(RESULT_CANCELED, mResultIntent);
                }
            }
        });





        /* Requesting the file:
                startActivityForResult() with Intent which has appropriate action and type
                in onActivityResult() if RESULT_OK:
                    Uri returnUri = returnIntent.getData();
                    mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
                    FileDescriptor fd = mInputPFD.getFileDescriptor();

            Requesting file information:
                String mimeType = getContentResolver().getType(returnUri);

                Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                String name = returnCursor.getString(nameIndex));
                Long size = returnCursor.getLong(sizeIndex);
         */
    }

    public void onDoneClick(View v) {
        finish();
    }
}
