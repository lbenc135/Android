package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.licoforen.parentalcontrollauncher.R;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;

public class ColorPick extends DialogPreference {

	ColorPicker picker;
	public int curVal;

	public ColorPick(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.colorpicker);
		setPositiveButtonText("OK");
		setNegativeButtonText("Cancel");
		setDialogIcon(null);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindView(view);

		picker = (ColorPicker) view.findViewById(R.id.picker);
		SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
		OpacityBar opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);
		SaturationBar saturationBar = (SaturationBar) view
				.findViewById(R.id.saturationbar);
		ValueBar valueBar = (ValueBar) view.findViewById(R.id.valuebar);

		picker.addSVBar(svBar);
		picker.addOpacityBar(opacityBar);
		picker.addSaturationBar(saturationBar);
		picker.addValueBar(valueBar);

		switch (getKey()) {
		case "drawerColor":
			picker.setOldCenterColor(ResourceLoader.drawerColor);
			picker.setColor(ResourceLoader.drawerColor);
			break;
		case "drawerLblColor":
			picker.setOldCenterColor(ResourceLoader.drawerLblColor);
			picker.setColor(ResourceLoader.drawerLblColor);
			break;
		case "homeLblColor":
			picker.setOldCenterColor(ResourceLoader.homeLblColor);
			picker.setColor(ResourceLoader.homeLblColor);
			break;
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			curVal = picker.getColor();
			persistInt(curVal);
		}
	}

}
