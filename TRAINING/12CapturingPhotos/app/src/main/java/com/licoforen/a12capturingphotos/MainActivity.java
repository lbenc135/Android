package com.licoforen.a12capturingphotos;

import android.app.Application;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class MainActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PackageManager pm = getPackageManager();
        // check that device has a camera (or in manifest uses-feature)
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA))
            finish();


        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("picture").setIndicator("Picture").setContent(new Intent(this, TakePictureActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("video").setIndicator("Video").setContent(new Intent(this, RecordVideoActivity.class)));
        tabHost.setCurrentTab(0);
    }

}
