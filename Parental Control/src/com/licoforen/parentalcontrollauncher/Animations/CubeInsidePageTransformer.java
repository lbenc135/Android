package com.licoforen.parentalcontrollauncher.Animations;

import android.view.View;
import android.support.v4.view.ViewPager;

public class CubeInsidePageTransformer implements ViewPager.PageTransformer {

	public void transformPage(View view, float position) {

		if (position < -1) {
			view.setRotationY(0);
		} else if (position <= 0) { // [-1,0]
			view.setRotationY(90 - (1 + position) * 90);
		} else if (position <= 1) { // (0,1]
			view.setRotationY(270 + (1 - position) * 90);
		} else { // (1,+Infinity]
			view.setRotationY(0);
		}
	}
}