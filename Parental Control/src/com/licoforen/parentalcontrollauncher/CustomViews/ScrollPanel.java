package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.licoforen.parentalcontrollauncher.R;
import com.licoforen.parentalcontrollauncher.Interfaces.DragSource;
import com.licoforen.parentalcontrollauncher.Interfaces.DropTarget;

public class ScrollPanel extends View implements DropTarget {

	ViewPager viewPager;

	public ScrollPanel(Context context) {
		super(context);
	}
	
	public ScrollPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ScrollPanel(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}
	
	public void setPager(ViewPager v) {
		viewPager = v;
	}

	@Override
	public boolean allowDrop(DragSource source) {
		return true;
	}

	@Override
	public void onDrop(DragSource source) {

	}

	@Override
	public void onDragEnter(DragSource source) {
		if (getId() == R.id.left_panel) {
			int prevItem = viewPager.getCurrentItem() - 1;
			if (prevItem >= 0)
				viewPager.setCurrentItem(prevItem, true);
		} else if (getId() == R.id.right_panel) {
			int nextItem = viewPager.getCurrentItem() + 1;
			if (nextItem < viewPager.getAdapter().getCount())
				viewPager.setCurrentItem(nextItem, true);
		}
	}

	@Override
	public void onDragExit(DragSource source) {

	}

}
