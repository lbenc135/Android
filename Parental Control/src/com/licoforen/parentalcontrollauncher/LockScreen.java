package com.licoforen.parentalcontrollauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;
import com.licoforen.parentalcontrollauncher.Helpers.SHA;

public class LockScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lock);

		final EditText passwordBox;

		passwordBox = (EditText) findViewById(R.id.pass_box);
		Button ok = (Button) findViewById(R.id.btnOk);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SHA.getHash(passwordBox.getText().toString()).equals(
						ResourceLoader.passwordHash)) {
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.wrong_password, Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button cancel = (Button) findViewById(R.id.btnCancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
				startHomescreen.addCategory(Intent.CATEGORY_HOME);
				startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(startHomescreen);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
	    return;
	}
}
