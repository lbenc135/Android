package com.licoforen.a11audioplayback;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    NoisyAudioStreamReceiver myNoisyAudioStreamReceiver;
    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // hardware volume keys control music stream
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // init mediaplayer and audiomanager
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.crvena_jabuka_imam_neke_fore);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        // listen for changes in audio focus
        AudioManager.OnAudioFocusChangeListener afChangeListener =
                new AudioManager.OnAudioFocusChangeListener() {
                    public void onAudioFocusChange(int focusChange) {
                        if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                            // Lower playback volume
                            mediaPlayer.setVolume(0.1f, 0.1f);
                            Log.d("MediaPlayer", "Audio focus loss transient - ducking");
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                            // Pause playback
                            mediaPlayer.pause();
                            Log.d("MediaPlayer", "Audio focus loss transient");
                        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                            // Resume playback
                            startPlayback();
                            Log.d("MediaPlayer", "Audio focus gain");
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            Log.d("MediaPlayer", "Audio focus loss");
                            am.unregisterMediaButtonEventReceiver(new ComponentName(getApplicationContext(), RemoteControlReceiver.class));
                            am.abandonAudioFocus(this);
                            // Stop playback
                            stopPlayback();
                        }
                    }
                };


        int result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus instead of transient.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // using hardware media keys (declare receiver in manifest)
            am.registerMediaButtonEventReceiver(new ComponentName(this, RemoteControlReceiver.class));

            // Play music
            try {
                //mediaPlayer.prepare();
                startPlayback();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (am.isBluetoothA2dpOn()) {
                // Adjust output for Bluetooth.
            } else if (am.isSpeakerphoneOn()) {
                // Adjust output for Speakerphone.
            } else if (am.isWiredHeadsetOn()) {
                // Adjust output for headsets
            } else {
                // If audio plays and noone can hear it, is it still playing?
            }
        }
    }

    private void startPlayback() {
        registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
        mediaPlayer.start();
    }

    private void stopPlayback() {
        unregisterReceiver(myNoisyAudioStreamReceiver);
        mediaPlayer.stop();
    }




    public class RemoteControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                Log.d("MediaPlayer", "Media key " + event.getKeyCharacterMap() + " pressed.");
                mediaPlayer.pause();
                if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == event.getKeyCode()) {
                    // Handle key press.
                }
            }
        }
    }

    // receive when audio becomes noisy (disconnected headphones on max volume)
    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                // Pause the playback
                Log.d("MediaPlayer", "Audio noisy");
                mediaPlayer.pause();
            }
        }
    }
}
