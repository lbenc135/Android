package com.licoforen.androidgames;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class SoundPoolTest extends Activity implements OnTouchListener {

	SoundPool soundpool;
	int explosionId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setOnTouchListener(this);
		setContentView(textview);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundpool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

		try {
			AssetManager assetmanager = getAssets();
			AssetFileDescriptor descriptor = assetmanager
					.openFd("explosion.ogg");
			explosionId = soundpool.load(descriptor, 1);
		} catch (IOException e) {
			textview.setText("Error! " + e.getMessage());
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (explosionId != -1) {
				soundpool.play(explosionId, 1, 1, 0, 0, 1);
			}
		}
		return true;
	}
}
