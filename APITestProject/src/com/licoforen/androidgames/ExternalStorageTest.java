package com.licoforen.androidgames;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class ExternalStorageTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		setContentView(textview);

		String state = Environment.getExternalStorageState();
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			textview.setText("No external storage mounted!");
		} else {
			File externaldir = Environment.getExternalStorageDirectory();
			File textfile = new File(externaldir.getAbsolutePath()
					+ File.separator + "text.txt");
			try {
				writeTextFile(textfile, "This is a test.");
				String text = readTextFile(textfile);
				textview.setText(text);
				if (!textfile.delete()) {
					textview.setText("Couldn't remove file.");
				}
			} catch (IOException e) {
				textview.setText("Error! " + e.getMessage());
			}
		}
	}

	public void writeTextFile(File file, String text) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(text);
		writer.close();
	}

	private String readTextFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder text = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			text.append(line);
			text.append('\n');
		}
		reader.close();
		return text.toString();
	}
}
