package com.licoforen.a8sharingdata;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ******* Sending text
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        //startActivity(sendIntent);

        // ******* Sending binary data (eg. image)
        Uri uriToImage = new Uri.Builder().build();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        //startActivity(Intent.createChooser(shareIntent, "Title"));

        //      The receiving application needs permission to access the data the Uri points to. The recommended ways to do this are:
        // Store the data in your own ContentProvider, making sure that other apps have the correct permission to access your provider.
        // The preferred mechanism for providing access is to use per-URI permissions which are temporary and only grant access to the receiving application.
        // An easy way to create a ContentProvider like this is to use the FileProvider helper class.
        //      Use the system MediaStore. The MediaStore is primarily aimed at video, audio and image MIME types,
        // however beginning with Android 3.0 (API level 11) it can also store non-media types (see MediaStore.Files for more info).
        // Files can be inserted into the MediaStore using scanFile() after which a content:// style Uri suitable for sharing is passed to the provided onScanCompleted() callback.
        // Note that once added to the system MediaStore the content is accessible to any app on the device.



        // ******* Sending multiple content
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        //imageUris.add(imageUri1);
        //imageUris.add(imageUri2);

        Intent shareIntent2 = new Intent();
        shareIntent2.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent2.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent2.setType("image/*");    // or image/jpeg if they're all jpegs, or */* if content is mixed
        //startActivity(Intent.createChooser(shareIntent2, "Share images to.."));

        // As before, make sure the provided URIs point to data that a receiving application can access.



        // ******** Easy share action
        // menu xml declaration
        // update mShareActionProvider shareIntent






        // ******** Receiving content
        // intent-filters in manifest
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (action.equals(Intent.ACTION_SEND) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }



    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }
}
