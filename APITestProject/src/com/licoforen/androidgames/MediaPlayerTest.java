package com.licoforen.androidgames;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class MediaPlayerTest extends Activity {
	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		setContentView(textview);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mp = new MediaPlayer();

		try {
			AssetManager assetmanager = getAssets();
			AssetFileDescriptor afd = assetmanager.openFd("music.ogg");
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			mp.prepare();
			mp.setLooping(true);
		} catch (IOException e) {
			textview.setText("Couldn't load music file, " + e.getMessage());
			mp = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mp != null) {
			mp.start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mp != null) {
			mp.pause();
			if (isFinishing()) {
				mp.stop();
				mp.release();
			}
		}
	}
}
