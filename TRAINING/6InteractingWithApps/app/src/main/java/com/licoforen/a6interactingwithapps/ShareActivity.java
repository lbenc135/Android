package com.licoforen.a6interactingwithapps;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        Intent intent = getIntent();
        Uri data = intent.getData();

        if(intent.getType().equals("text/plain")) {
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
