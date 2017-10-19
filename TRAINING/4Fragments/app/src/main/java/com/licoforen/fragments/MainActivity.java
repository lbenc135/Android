package com.licoforen.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    boolean frag1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void replaceFragment(View view) {
        if(frag1) {
            Fragment2 fragment2 = new Fragment2();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment2).commit();
        } else {
            Fragment1 fragment1 = new Fragment1();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1).commit();
        }
        frag1=!frag1;
    }
}
