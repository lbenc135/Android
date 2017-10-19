package com.licoforen.cloverstudioapplication;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    EditText etNumber;
    TextView txtSmallerNumber, txtGreaterNumber, lblMessages;

    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 100;

    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();

        etNumber = (EditText) findViewById(R.id.etNumber);
        txtSmallerNumber = (TextView) findViewById(R.id.txtNumberSmaller);
        txtGreaterNumber = (TextView) findViewById(R.id.txtNumberGreater);
        lblMessages = (TextView) findViewById(R.id.lblMessages);

        initializeSpeechRecognition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                }
            }
        }
    }


    public void btnCalculateClick(View button) {
        String number = etNumber.getText().toString();
        lblMessages.setText("");

        number = processInput(number);
        if (!number.isEmpty()) {
            // calculate numbers
            String smallerNumber = findNumber(number, true);
            String greaterNumber = findNumber(number, false);

            // fancy animations
            Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.anim.animations);
            txtSmallerNumber.startAnimation(zoomAnimation);
            txtGreaterNumber.startAnimation(zoomAnimation);


            txtSmallerNumber.setText(smallerNumber);
            txtGreaterNumber.setText(greaterNumber);
        }

        // hide keyboard
        if (button != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(button.getWindowToken(), 0);
        }
    }

    public void btnSpeechRecognition(View button) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }
    }

    private String processInput(String num) {
        num = num.replaceAll("[^0-9]", "");
        Integer number = 0;

        if (num.isEmpty()) {
            lblMessages.setText(res.getText(R.string.errorEnterNumber));
            return num;
        } else {
            number = Integer.parseInt(num);
            if (number < 100 || number > 10000)
                lblMessages.setText(res.getText(R.string.warningConstraintsDisregarded));
        }

        return number.toString();
    }

    private String findNumber(String n, boolean smaller) {
        char[] num = n.toCharArray();
        boolean successful = false;

        int nlen = n.length();
        for (int i = nlen - 1; i >= 1; i--) {
            if (num[i] < num[i - 1] && smaller ||
                    num[i] > num[i - 1] && !smaller) {

                // swap digits
                char c = num[i];
                num[i] = num[i - 1];
                num[i - 1] = c;

                // sort rest of array
                Arrays.sort(num, i, nlen);
                // reverse rest of array
                if (smaller)
                    for (int j = 0; j < (nlen - i) / 2; j++) {
                        char t = num[j + i];
                        num[j + i] = num[nlen - 1 - j];
                        num[nlen - 1 - j] = t;
                    }

                successful = true;
                break;
            }
        }

        if (successful)
            return new String(num);
        else
            return res.getString(R.string.noSuchNumber);
    }


    private void initializeSpeechRecognition() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                2000);

        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
    }

    private class SpeechRecognitionListener implements RecognitionListener {
        private String TAG = "Speech Req Module";

        @Override
        public void onBeginningOfSpeech() {
            lblMessages.setText(res.getText(R.string.infoListening));
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            Log.d(TAG, "Error " + error);
            lblMessages.setText(res.getText(R.string.errorNoNumberHeard));
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onResults(Bundle results) {
            String match = "";
            // get most likely match
            if (results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) != null)
                match = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);

            match = processInput(match);
            if (!match.isEmpty()) {
                etNumber.setText(match);
                lblMessages.setText("");
                btnCalculateClick(null);
            } else
                lblMessages.setText(res.getText(R.string.errorNoNumberHeard));
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }
    }
}
