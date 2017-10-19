package com.licoforen.parentalcontrollauncher.CustomViews;

import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.licoforen.parentalcontrollauncher.R;

public class WidgetHostView extends AppWidgetHostView {
	private boolean mHasPerformedLongPress;

	private CheckForLongPress mPendingCheckForLongPress;

	private LayoutInflater mInflater;

	public float startX, startY;
	public int w, h, startCell;
	private int sX = 0, sY = 0;
	private int touchSlop;

	public WidgetHostView(Context context) {
		super(context);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		touchSlop = touchSlop * touchSlop;
	}

	@Override
	protected View getErrorView() {
		return mInflater.inflate(R.layout.widget_error, this, false);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mHasPerformedLongPress) {
			mHasPerformedLongPress = false;
			return true;
		}

		int tX, tY;

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			sY = (int) ev.getRawY();
			sX = (int) ev.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mPendingCheckForLongPress == null) {
				postCheckForLongClick();
			}
			postCheckForLongClick();

			tX = (int) ev.getRawX();
			tY = (int) ev.getRawY();
			if ((sX - tX) * (sX - tX) + (sY - tY) * (sY - tY) >= touchSlop) {
				cancelLongPress();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			cancelLongPress();
			break;
		}

		return false;
	}

	class CheckForLongPress implements Runnable {
		private int mOriginalWindowAttachCount;

		public void run() {
			if ((getParent() != null) && hasWindowFocus()
					&& mOriginalWindowAttachCount == getWindowAttachCount()
					&& !mHasPerformedLongPress) {
				if (performLongClick()) {
					mHasPerformedLongPress = true;
				}
			}
		}

		public void rememberWindowAttachCount() {
			mOriginalWindowAttachCount = getWindowAttachCount();
		}
	}

	private void postCheckForLongClick() {
		mHasPerformedLongPress = false;
		if (mPendingCheckForLongPress == null) {
			mPendingCheckForLongPress = new CheckForLongPress();
		}
		mPendingCheckForLongPress.rememberWindowAttachCount();
		postDelayed(mPendingCheckForLongPress,
				ViewConfiguration.getLongPressTimeout());
	}

	@Override
	public void cancelLongPress() {
		super.cancelLongPress();
		mHasPerformedLongPress = false;
		if (mPendingCheckForLongPress != null) {
			removeCallbacks(mPendingCheckForLongPress);
		}
	}
}
