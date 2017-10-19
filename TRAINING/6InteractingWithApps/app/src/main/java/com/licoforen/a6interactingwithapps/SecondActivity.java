package com.licoforen.a6interactingwithapps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        int first = intent.getIntExtra("com.licoforen.a6interactingwithapps.firstnum", 0);
        int second = intent.getIntExtra("com.licoforen.a6interactingwithapps.secondnum", 0);

        Intent result = new Intent();
        result.putExtra("com.licoforen.a6interactingwithapps.mynumber", first+second);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
