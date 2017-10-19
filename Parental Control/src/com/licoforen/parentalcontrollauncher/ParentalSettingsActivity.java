package com.licoforen.parentalcontrollauncher;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.licoforen.parentalcontrollauncher.CustomViews.PasswordCheck;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;
import com.licoforen.parentalcontrollauncher.Helpers.SHA;

public class ParentalSettingsActivity extends PreferenceActivity {

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.parentalpreferences);
		setResult(RESULT_OK);

		MultiSelectListPreference apps = (MultiSelectListPreference) findPreference("allowedApps");
		MainActivity.Pack appslist[] = MainActivity.packs;
		int length = appslist.length;
		CharSequence entryvalues[] = new CharSequence[length], entries[] = new CharSequence[length];
		for (int i = 0; i < length; i++) {
			entryvalues[i] = appslist[i].packageName;
			entries[i] = appslist[i].label;
		}
		apps.setEntryValues(entryvalues);
		apps.setEntries(entries);

		MultiSelectListPreference widgets = (MultiSelectListPreference) findPreference("allowedWidgets");
		List<AppWidgetProviderInfo> widgetslist = MainActivity.widgetList;
		length = widgetslist.size();
		entryvalues = new CharSequence[length];
		entries = new CharSequence[length];
		for (int i = 0; i < length; i++) {
			entryvalues[i] = widgetslist.get(i).provider.getClassName();
			entries[i] = widgetslist.get(i).label;
		}
		widgets.setEntryValues(entryvalues);
		widgets.setEntries(entries);

		PasswordCheck passcheck = (PasswordCheck) findPreference("change_pass");
		passcheck.setContext(this);

		LayoutInflater inflater = this.getLayoutInflater();
		View layout = inflater.inflate(R.layout.passwordcheck, null);
		final EditText passwordBox;
		final AlertDialog d = new AlertDialog.Builder(this)
				.setTitle("Password check").setView(layout)
				.setNegativeButton("Cancel", null)
				.setPositiveButton("OK", null).setCancelable(false).create();

		passwordBox = (EditText) layout.findViewById(R.id.password_box);

		d.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(
						InputMethodManager.SHOW_FORCED, 0);

				Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (SHA.getHash(passwordBox.getText().toString())
								.equals(ResourceLoader.passwordHash)) {
							d.dismiss();
							InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							inputMethodManager.toggleSoftInput(
									InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
						} else {
							Toast.makeText(getApplicationContext(),
									R.string.wrong_password, Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
				Button bn = d.getButton(AlertDialog.BUTTON_NEGATIVE);
				bn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.toggleSoftInput(
								InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

						finish();
					}
				});
			}
		});
		d.show();
	}
}
