package com.licoforen.androidgames;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;

public class KeyTest extends Activity implements OnKeyListener {
	StringBuilder builder = new StringBuilder();
	TextView textview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textview = new TextView(this);
		textview.setText("Press keys");
		textview.setOnKeyListener(this);
		textview.setFocusableInTouchMode(true);
		textview.requestFocus();
		setContentView(textview);
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		builder.setLength(0);
		switch(event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			builder.append("down, ");
			break;
		case KeyEvent.ACTION_UP:
			builder.append("up, ");
			break;
		}
		builder.append(event.getKeyCode());
		builder.append(", ");
		builder.append((char) event.getUnicodeChar());
		String text = builder.toString();
		Log.d("KeyTest", text);
		textview.setText(text);
		return event.getKeyCode() != KeyEvent.KEYCODE_BACK;
	}
}
