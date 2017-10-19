package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.licoforen.parentalcontrollauncher.R;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;

public class LogPref extends DialogPreference {

	private TextView textview;

	public LogPref(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.activitylog);
		setPositiveButtonText("Clear log");
		setNegativeButtonText("OK");
		setDialogIcon(null);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		if(positiveResult)
			ResourceLoader.clearLog();
	}
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindView(view);
		textview = (TextView) view.findViewById(R.id.textview);
		textview.setText(ResourceLoader.activityLog);
		textview.setPadding(10, 10, 10, 10);
	}

}
