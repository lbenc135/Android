package com.licoforen.parentalcontrollauncher.Animations;

import android.view.View;
import android.support.v4.view.ViewPager;

public class FadePageTransformer implements ViewPager.PageTransformer {

	public void transformPage(View view, float position) {

		if (position < -1) {
			view.setAlpha(0);

		} else if (position <= 0) { // [-1,0]
			view.setAlpha(1 + position);

		} else if (position <= 1) { // (0,1]

			view.setAlpha(1 - position);

		} else { // (1,+Infinity]
			view.setAlpha(0);
		}
	}
}