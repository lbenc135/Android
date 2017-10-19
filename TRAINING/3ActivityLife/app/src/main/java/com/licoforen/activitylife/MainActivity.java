package com.licoforen.activitylife;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    String log;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.log);
        log += "onCreate() - called only once\n";
        updateText();

        // instancirati class-wide varijable
        // prilikom mijenanja orijentacije cijeli activity se unisti i ponovo zove - savedInstanceState sadrzi spremljene podatke od proslog activityja
    }

    @Override
    protected void onStart() {
        super.onStart();
        log += "onStart() - created or restarted\n";
        updateText();

        // sve suprotno od onStop
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log += "onRestart() - restarted\n";
        updateText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        log += "onResume() - now running\n";
        updateText();

        // sve suprotno od onPause
    }

    @Override
    protected void onPause() {
        super.onPause();
        log += "onPause() - partially visible or stopping\n";
        updateText();

        // tu pauzirati animacije, spremiti lagane podatke (ne u bazu), otpustiti resurse - CPU non intensive operations
    }

    @Override
    protected void onStop() {
        super.onStop();
        log += "onStop() - stopped, hidden\n";
        updateText();

        // tu spremati u bazu, otpustiti sve resurse - mozda zadnja funkcija koja se zove
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroying activity...", Toast.LENGTH_SHORT).show();

        // ukinuti long-runing operacije
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        // tu spremiti dodatne podatke za restore activityja
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        // tu ili u onCreate restoreati spremljene dodatne podatke
    }

    private void updateText()
    {
        textView.setText(log);
    }
}
