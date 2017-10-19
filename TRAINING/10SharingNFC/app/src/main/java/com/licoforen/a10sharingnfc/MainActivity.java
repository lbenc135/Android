package com.licoforen.a10sharingnfc;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    NfcAdapter mNfcAdapter;
    // Flag to indicate that Android Beam is available
    boolean mAndroidBeamAvailable  = false;
    private Uri[] mFileUris = new Uri[10];
    private FileUriCallback mFileUriCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permissions in manifest


        // NFC isn't available on the device
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
            /*
             * Disable NFC features here.
             * For example, disable menu items or buttons that activate
             * NFC-related features
             */
            // Android Beam file transfer isn't supported
        } else if (Build.VERSION.SDK_INT <
                Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // If Android Beam isn't available, don't continue.
            mAndroidBeamAvailable = false;
            /*
             * Disable Android Beam file transfer features here.
             */
            // Android Beam file transfer is available, continue
        } else {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

            /*
             * Instantiate a new FileUriCallback to handle requests for
             * URIs
             */
            mFileUriCallback = new FileUriCallback();
            // Set the dynamic callback for URI requests.
            mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback, this);

            // can also use NfcAdapter.setBeamPushUris()


            
            /*
             * Create a list of URIs, get a File,
             * and set its permissions
             */
            String transferFile = "transferimage.jpg";
            File extDir = getExternalFilesDir(null);
            File requestFile = new File(extDir, transferFile);
            requestFile.setReadable(true, false);
            // Get a URI for the File and add it to the list of URIs
            Uri fileUri = Uri.fromFile(requestFile);
            if (fileUri != null) {
                mFileUris[0] = fileUri;
            } else {
                Log.e("My Activity", "No File URI available for file.");
            }
        }
    }


    private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {
        public FileUriCallback() {
        }
        /**
         * Create content URIs as needed to share with another device
         */
        @Override
        public Uri[] createBeamUris(NfcEvent event) {
            return mFileUris;
        }
    }
}
