package com.licoforen.parentalcontrollauncher.Helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import android.content.Context;
import android.content.Intent;

public class Database {

	FileOutputStream fo;
	FileInputStream fi;
	String homeFile = "homedata", dockFile = "dockdata";
	Context mContext;

	public Database(Context c) {
		mContext = c;
	}

	public boolean writeData(Intent home[][], Intent dock[], int homeItems,
			int homeScreens) {

		boolean success = true;
		try {
			fo = mContext.openFileOutput(homeFile, Context.MODE_PRIVATE);
			for (int k = 0; k < homeScreens; k++) {
				for (int i = 0; i < homeItems; i++) {
					if (home[k][i] != null) {
						fo.write(home[k][i].toUri(0).getBytes());
					}
					fo.write((byte) 255);
				}
			}
			fo.close();
			fo = mContext.openFileOutput(dockFile, Context.MODE_PRIVATE);
			for (int i = 0; i < 5; i++) {
				if (dock[i] != null) {
					fo.write(dock[i].toUri(0).getBytes());
				}
				fo.write((byte) 255);
			}
			fo.close();
		} catch (Exception e) {
			success = false;
		}

		return success;
	}

	public Intent[][] readHomeData(int homeItems, int homeScreens) {
		Intent[][] array = new Intent[homeScreens][homeItems];
		Intent temp;
		try {
			fi = mContext.openFileInput(homeFile);
			for (int k = 0; k < homeScreens; k++) {
				for (int i = 0; i < homeItems; i++) {
					temp = readLine();
					if (temp != null) {
						int x = Integer.parseInt(temp.getStringExtra("xPos"));
						int y = Integer.parseInt(temp.getStringExtra("yPos"));
						int scr = Integer.parseInt(temp
								.getStringExtra("scrPos"));
						array[scr][y * ResourceLoader.numHomeCols + x] = temp;
					}
				}
			}
			fi.close();
		} catch (Exception e) {
		}
		return array;
	}

	public Intent[] readDockData() {
		Intent[] array = new Intent[5];
		try {
			fi = mContext.openFileInput(dockFile);
			for (int i = 0; i < 5; i++)
				array[i] = readLine();
			fi.close();
		} catch (Exception e) {
		}
		return array;
	}

	private Intent readLine() {
		String line = null;
		Intent cp = null;
		int b;
		byte bt[] = new byte[1];
		while (true) {
			try {
				b = fi.read();
				if (b == 255 || b == -1) {
					break;
				} else {
					bt[0] = (byte) b;
					String letter = new String(bt);
					if (line == null)
						line = letter;
					else
						line += letter;
				}

			} catch (IOException e) {
			}
		}

		if (line != null)
			try {
				cp = Intent.parseUri(line, 0);
			} catch (URISyntaxException e) {
			}

		return cp;
	}
}
