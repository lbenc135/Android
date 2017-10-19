package com.licoforen.androidgames;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class SingleTouchTest extends Activity implements OnTouchListener {
	StringBuilder builder = new StringBuilder();
	TextView textview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textview = new TextView(this);
		textview.setText("Touch and drag (on finger only!");
		textview.setOnTouchListener(this);
		setContentView(textview);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		builder.setLength(0);
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			builder.append("down, ");
			break;
		case MotionEvent.ACTION_MOVE:
			builder.append("move, ");
			break;
		case MotionEvent.ACTION_CANCEL:
			builder.append("cancel, ");
			break;
		case MotionEvent.ACTION_UP:
			builder.append("up, ");
			break;
		}
		builder.append(event.getX());
		builder.append(", ");
		builder.append(event.getY());
		String text = builder.toString();
		Log.d("TouchTest", text);
		textview.setText(text);
		return true;
	}
}
