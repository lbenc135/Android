package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Toast;

import com.licoforen.parentalcontrollauncher.Interfaces.DragSource;
import com.licoforen.parentalcontrollauncher.Interfaces.DropTarget;

public class DeleteZone extends ImageView implements DropTarget {

	public DeleteZone(Context context) {
		super(context);
	}

	public DeleteZone(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DeleteZone(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}

	public boolean allowDrop(DragSource source) {
		return true;
	}

	public void onDrop(DragSource source) {
		toast("Moved to trash");
		invalidate();
		clearColorFilter();
	}

	public void onDragEnter(DragSource source) {
		setColorFilter(Color.RED);
	}

	public void onDragExit(DragSource source) {
		clearColorFilter();
	}

	public void toast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}

}
