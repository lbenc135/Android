package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.licoforen.parentalcontrollauncher.R;

public class SeekbarPref extends DialogPreference implements
		OnSeekBarChangeListener {

	private SeekBar seekbar;
	private TextView textview;
	public int curVal;

	public SeekbarPref(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.seekbarpicker);
		setPositiveButtonText("OK");
		setNegativeButtonText("Cancel");
		setDialogIcon(null);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindView(view);
		seekbar = (SeekBar) view.findViewById(R.id.seekbar);
		textview = (TextView) view.findViewById(R.id.textview);
		switch (getKey()) {
		case "animLength":
			curVal = this.getPersistedInt(150);
			seekbar.setMax(500);
			textview.setText(Integer.toString(curVal) + " ms");
			break;
		}
		seekbar.setProgress(curVal);
		seekbar.setPadding(20, 10, 20, 10);
		seekbar.setOnSeekBarChangeListener(this);
		textview.setPadding(10, 10, 10, 10);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			curVal = seekbar.getProgress();
			persistInt(curVal);
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
		seekbar.setProgress(myState.value);
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		curVal = seekbar.getProgress();
		switch (getKey()) {
		case "animLength":
			textview.setText(Integer.toString(curVal));
			break;
		case "drawerTransp":
			textview.setText(Integer.toString(curVal) + "%");
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
