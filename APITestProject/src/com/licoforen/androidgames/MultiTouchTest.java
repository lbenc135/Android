package com.licoforen.androidgames;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

@TargetApi(8)
public class MultiTouchTest extends Activity implements OnTouchListener {
	StringBuilder builder = new StringBuilder();
	TextView textview;
	float[] x = new float[10];
	float[] y = new float[10];
	boolean[] touched = new boolean[10];
	int[] id = new int[10];

	private void updateTextView() {
		builder.setLength(0);
		for (int i = 0; i < 10; i++) {
			builder.append(touched[i]);
			builder.append(", ");
			builder.append(id[i]);
			builder.append(", ");
			builder.append(x[i]);
			builder.append(", ");
			builder.append(y[i]);
			builder.append('\n');
		}
		textview.setText(builder.toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textview = new TextView(this);
		textview.setText("Touch and drag (multiple fingers supported)!");
		textview.setOnTouchListener(this);
		setContentView(textview);
		for (int i = 0; i < 10; i++) {
			id[i] = -1;
		}
		updateTextView();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		int pointerindex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		int pointercount = event.getPointerCount();
		for (int i = 0; i < 10; i++) {
			if (i >= pointercount) {
				touched[i] = false;
				id[i] = -1;
				continue;
			}
			if (event.getAction() != MotionEvent.ACTION_MOVE
					&& i != pointerindex) {
				continue;
			}
			int pointerId = event.getPointerId(i);
			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touched[i] = true;
				id[i] = pointerId;
				x[i] = (int) event.getX();
				y[i] = (int) event.getY();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
				touched[i] = false;
				id[i] = -1;
				x[i] = (int) event.getX(i);
				y[i] = (int) event.getY(i);
				break;
			case MotionEvent.ACTION_MOVE:
				touched[i] = true;
				id[i] = pointerId;
				x[i] = (int) event.getX(i);
				y[i] = (int) event.getY(i);
				break;
			}
		}
		updateTextView();
		return true;
	}
}
