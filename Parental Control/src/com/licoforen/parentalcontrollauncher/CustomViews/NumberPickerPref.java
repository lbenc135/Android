package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.licoforen.parentalcontrollauncher.R;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;

public class NumberPickerPref extends DialogPreference {

	private NumberPicker numPicker;
	public int curVal;

	public NumberPickerPref(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.desktopgridpicker);
		setPositiveButtonText("OK");
		setNegativeButtonText("Cancel");
		setDialogIcon(null);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindView(view);

		numPicker = (NumberPicker) view.findViewById(R.id.numPicker);
		numPicker.setMinValue(1);

		switch (getKey()) {
		case "numHomeRows":
			numPicker.setMaxValue(ResourceLoader.defaultNumRows + 1);
			curVal = this.getPersistedInt(ResourceLoader.numHomeRows);
			break;
		case "numHomeCols":
			numPicker.setMaxValue(ResourceLoader.defaultNumCols + 1);
			curVal = this.getPersistedInt(ResourceLoader.numHomeCols);
			break;
		case "homeScreens":
			numPicker.setMaxValue(10);
			curVal = this.getPersistedInt(5);
			break;
		case "defaultScreen":
			curVal = this.getPersistedInt(3);
			numPicker.setMaxValue(ResourceLoader.homeScreens);
			break;
		}

		numPicker.setValue(curVal);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			curVal = numPicker.getValue();
			persistInt(curVal);
			if (getKey().equals("homeScreens")) {
				ResourceLoader.homeScreens = curVal;
			}
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		if (isPersistent()) {
			return superState;
		}
		final SavedState myState = new SavedState(superState);
		myState.value = curVal;
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		numPicker.setValue(myState.value);
	}

	private static class SavedState extends BaseSavedState {
		int value;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
			value = source.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(value);
		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
