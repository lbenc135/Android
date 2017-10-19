package com.licoforen.suppdiffdevs;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.version_text);
        textView.setText("I am running SDK: " + Build.VERSION.SDK_INT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            Toast.makeText(this, "Jupi I'm running modern Android", Toast.LENGTH_SHORT).show();
    }
}
