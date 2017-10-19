package com.licoforen.androidgames;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

public class AssetsTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		setContentView(textview);

		AssetManager assetmanager = getAssets();
		InputStream inputstream = null;
		try {
			inputstream = assetmanager.open("texts/myawesometext.txt");
			String text = loadTextFile(inputstream);
			textview.setText(text);
		} catch (IOException e) {
			textview.setText("Couldn't load file");
		} finally {
			if (inputstream != null)
				try {
					inputstream.close();
				} catch (IOException e) {
					textview.setText("Couldn't load file");
				}
		}
	}
	
	public String loadTextFile(InputStream inputstream) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		byte[] bytes = new byte[4096];
		int len = 0;
		while((len=inputstream.read(bytes)) > 0)
			bytestream.write(bytes, 0, len);
		return new String(bytestream.toByteArray(), "UTF8");
	}
}
