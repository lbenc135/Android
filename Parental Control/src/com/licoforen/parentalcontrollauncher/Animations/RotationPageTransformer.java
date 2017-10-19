package com.licoforen.parentalcontrollauncher.Animations;

import android.view.View;
import android.support.v4.view.ViewPager;

public class RotationPageTransformer implements ViewPager.PageTransformer {

	public void transformPage(View view, float position) {

		if (position < -1) {
			view.setRotation(0);
		} else if (position <= 0) { // [-1,0]
			view.setRotation(position * 90);
		} else if (position <= 1) { // (0,1]
			view.setRotation(position * 90);
		} else { // (1,+Infinity]
			view.setRotation(0);
		}
	}
}