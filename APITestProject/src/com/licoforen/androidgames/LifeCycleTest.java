package com.licoforen.androidgames;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LifeCycleTest extends Activity {
	StringBuilder builder = new StringBuilder();
	TextView textview;
	
	private void log(String text) {
		Log.d("LifeCycleTest", text);
		builder.append(text);
		builder.append('\n');
		textview.setText(builder.toString());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textview = new TextView(this);
		textview.setText(builder.toString());
		setContentView(textview);
		log("created");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		log("resumed");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		log("paused");
		
		if(isFinishing()) {
			log("finishing");
		}
	}
}
