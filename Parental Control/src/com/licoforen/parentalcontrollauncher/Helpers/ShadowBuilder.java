package com.licoforen.parentalcontrollauncher.Helpers;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.DragShadowBuilder;

import com.licoforen.parentalcontrollauncher.CustomViews.ImageCell;

public class ShadowBuilder extends DragShadowBuilder {

	Point touchPoint;
	private Drawable shadow;

	public ShadowBuilder(View v, Point touchPoint) {
		super(v);
		this.touchPoint = touchPoint;
		shadow = ((ImageCell) v).icon.getDrawable();
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		shadow.draw(canvas);
	}

	@Override
	public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
		shadowSize.set(shadow.getIntrinsicWidth(), shadow.getIntrinsicHeight());
		shadowTouchPoint.set(touchPoint.x, touchPoint.y);
	}
}
