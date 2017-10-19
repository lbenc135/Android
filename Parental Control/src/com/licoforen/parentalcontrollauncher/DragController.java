package com.licoforen.parentalcontrollauncher;

import android.content.ClipData;
import android.graphics.Point;
import android.view.DragEvent;
import android.view.View;

import com.licoforen.parentalcontrollauncher.Helpers.ShadowBuilder;
import com.licoforen.parentalcontrollauncher.Interfaces.DragDropPresenter;
import com.licoforen.parentalcontrollauncher.Interfaces.DragSource;
import com.licoforen.parentalcontrollauncher.Interfaces.DropTarget;

public class DragController implements View.OnDragListener {

	private boolean mDragging;
	private boolean mDropSuccess;

	private DragSource mDragSource;
	private static DropTarget mDropTarget;

	private DragDropPresenter mPresenter;

	public DragController(DragDropPresenter p) {
		mPresenter = p;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		
		boolean isDragSource = false, isDropTarget = false;
		DragSource source = null;
		DropTarget target = null;

		try {
			source = (DragSource) v;
			isDragSource = true;
		} catch (ClassCastException e) {
		}
		try {
			target = (DropTarget) v;
			isDropTarget = true;
		} catch (ClassCastException e) {
		}

		boolean eventResult = false;

		switch (event.getAction()) {

		case DragEvent.ACTION_DRAG_STARTED:

			if (!mDragging) {
				mDragging = true;
				mDropSuccess = false;
				if (mPresenter != null)
					mPresenter.onDragStarted(mDragSource);
			}

			if (isDragSource) {
				if (source == mDragSource) {
					if (source.allowDrag()) {
						eventResult = true;
						source.onDragStarted();
					}
				} else {
					eventResult = isDropTarget && target.allowDrop(mDragSource);
				}
			} else if (isDropTarget) {
				eventResult = target.allowDrop(mDragSource);
			} else
				eventResult = false;
			break;

		case DragEvent.ACTION_DRAG_ENTERED:
			if (isDropTarget) {
				target.onDragEnter(mDragSource);
				mDropTarget = target;
				eventResult = true;
			} else
				eventResult = false;
			break;

		case DragEvent.ACTION_DRAG_EXITED:
			if (isDropTarget) {
				mDropTarget = null;
				target.onDragExit(mDragSource);
				eventResult = true;
			} else
				eventResult = false;
			break;

		case DragEvent.ACTION_DROP:
			if (isDropTarget) {
				if (target.allowDrop(mDragSource)) {
					target.onDrop(mDragSource);
					mDropTarget = target;
					mDropSuccess = true;
				}
				eventResult = true;
			} else
				eventResult = false;
			break;

		case DragEvent.ACTION_DRAG_ENDED:
			if (mDragging) {
				if (mDragSource != null)
					mDragSource.onDropCompleted(mDropTarget, mDropSuccess);
				if (mPresenter != null)
					mPresenter.onDropCompleted(mDropTarget, mDragSource,
							mDropSuccess);
				eventResult = true;
			}
			mDragging = false;
			mDragSource = null;
			mDropTarget = null;
			break;
		}
		return eventResult;

	}

	public boolean startDrag(View v, int wh) {

		boolean isDragSource = false;
		DragSource ds = null;
		try {
			ds = (DragSource) v;
			isDragSource = true;
		} catch (ClassCastException ex) {
		}

		if (!isDragSource)
			return false;

		if (!ds.allowDrag())
			return false;

		mDragging = false;
		mDropSuccess = false;
		mDragSource = ds;
		mDropTarget = null;

		ClipData dragData = ds.clipDataForDragDrop();
		ShadowBuilder shadowView = new ShadowBuilder(v, new Point(
				(int) (wh * 0.6), (int) (wh * 0.6)));
		v.startDrag(dragData, shadowView, null, 0);
		return true;
	}
}
