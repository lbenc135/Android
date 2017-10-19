package com.licoforen.parentalcontrollauncher.Animations;

import android.view.View;
import android.support.v4.view.ViewPager;

public class ScaleXPageTransformer implements ViewPager.PageTransformer {

	public void transformPage(View view, float position) {

		if (position < -1) {
			view.setScaleX(0);
		} else if (position <= 0) { // [-1,0]
			view.setScaleX(1 + position);
		} else if (position <= 1) { // (0,1]
			view.setScaleX(1 - position);
		} else { // (1,+Infinity]
			view.setScaleX(0);
		}
	}
}