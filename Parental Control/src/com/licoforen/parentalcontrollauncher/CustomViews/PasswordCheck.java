package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.licoforen.parentalcontrollauncher.R;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;

public class PasswordCheck extends DialogPreference {

	private EditText passwordBox, passwordConfirmBox;
	private Context mContext;

	public PasswordCheck(Context context, AttributeSet attrs) {
		super(context, attrs);

		switch (getKey()) {
		case "change_pass":
			setDialogLayoutResource(R.layout.passwordcreate);
			break;
		}

		setDialogIcon(null);
	}

	public void setContext(Context c) {
		mContext = c;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindView(view);

		passwordBox = (EditText) view.findViewById(R.id.password_box);
		passwordConfirmBox = (EditText) view
				.findViewById(R.id.password_box_confirm);

		InputMethodManager inputMethodManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			if (getKey().equals("change_pass")) {
				if (passwordBox.getText().toString()
						.equals(passwordConfirmBox.getText().toString())) {
					ResourceLoader
							.setPassword(passwordBox.getText().toString());
					Toast.makeText(getContext(), "Password changed!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getContext(), "Passwords don't match!",
							Toast.LENGTH_SHORT).show();
					showDialog(null);
				}
			}
		}

		InputMethodManager inputMethodManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
}
