package com.licoforen.parentalcontrollauncher.Adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

public class HomePagerAdapter extends PagerAdapter {

	GridLayout pages[];

	public HomePagerAdapter(GridLayout homeGrid[]) {
		pages = homeGrid;
	}

	public void updateGrid(GridLayout homeGrid[]) {
		pages = homeGrid;
	}

	@Override
	public int getCount() {
		return pages.length;
	}

	@Override
	public boolean isViewFromObject(View v, Object o) {
		return v == (GridLayout) o;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int pos) {
		container.addView(pages[pos]);
		return pages[pos];
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
