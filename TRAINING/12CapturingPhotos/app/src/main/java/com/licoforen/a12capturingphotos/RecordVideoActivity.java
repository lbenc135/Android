package com.licoforen.a12capturingphotos;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordVideoActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 2;
    private VideoView videoView;
    private MediaController mediaController;
    String currentVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
    }

    public void onBtnRecordVideo(View v) {
        dispatchTakeVideoIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CAPTURE) {
                Uri videoUri = data.getData();

                try {
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(videoUri);
                    videoView.seekTo(0);
                    videoView.start();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException e) {
                Toast.makeText(this, "Failed to create video path.", Toast.LENGTH_SHORT).show();
            }

            if (videoFile != null) {
                // declare fileprovider in manifest + create xml filepaths
                Uri videoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }

        } else {
            Toast.makeText(this, "No video app", Toast.LENGTH_SHORT).show();
        }
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(imageFileName, ".mp4", storageDir);
        currentVideoPath = video.getAbsolutePath();
        return video;
    }
}
