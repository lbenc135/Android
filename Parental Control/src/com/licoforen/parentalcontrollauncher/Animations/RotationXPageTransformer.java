package com.licoforen.parentalcontrollauncher.Animations;

import android.view.View;
import android.support.v4.view.ViewPager;

public class RotationXPageTransformer implements ViewPager.PageTransformer {

	public void transformPage(View view, float position) {

		if (position < -1) {
			view.setRotationX(0);
		} else if (position <= 0) { // [-1,0]
			view.setRotationX(90 - (1 + position) * 90);
		} else if (position <= 1) { // (0,1]
			view.setRotationX(90 - (1 - position) * 90);
		} else { // (1,+Infinity]
			view.setRotationX(0);
		}
	}
}