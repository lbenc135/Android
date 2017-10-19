package com.licoforen.parentalcontrollauncher.Helpers;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

public class ResourceLoader {

	public static int numHomeRows, numHomeCols, homePadding, dockPadding,
			dockHeight, animLength, homeScreens, drawerColor, defaultScreen,
			defaultNumRows, defaultNumCols, homeLblColor, drawerLblColor;
	public static boolean homescrlabels, shortcutsAllowed, wallpaperScrolling,
			homescrShadows, drawerscrShadows, lockDesktop, activityCheck, locTrack;
	public static String passwordHash, activityLog, homeAnimation,
			homePageIndicator, homeOrientation, dockBackground, locUpdate;
	public static Set<String> allowedApps, allowedWidgets;

	static SharedPreferences sp;
	static SharedPreferences.Editor editor;

	public ResourceLoader(Context c, int width, int height, float scale) {
		sp = c.getSharedPreferences(
				"com.licoforen.parentalcontrollauncher_preferences",
				Context.MODE_PRIVATE);
		editor = sp.edit();

		defaultNumRows = (height - (int) ((dockHeight + 30) * scale + 0.5f))
				/ (int) (80 * scale + 0.5f);
		defaultNumCols = width / (int) (80 * scale + 0.5f);

		homePadding = sp.getInt("homePadding", 3);
		dockPadding = sp.getInt("dockPadding", 5);
		dockHeight = sp.getInt("dockHeight", 70);
		animLength = sp.getInt("animLength", 150);
		homeScreens = sp.getInt("homeScreens", 5);
		numHomeRows = sp.getInt("numHomeRows", defaultNumRows);
		numHomeCols = sp.getInt("numHomeCols", defaultNumCols);
		homescrlabels = sp.getBoolean("homescrLabels", true);
		drawerColor = sp.getInt("drawerColor", 0x7f000000);
		passwordHash = sp.getString("passwordHash", "");
		allowedApps = sp.getStringSet("allowedApps", null);
		activityLog = sp.getString("activityLog", "");
		homeAnimation = sp.getString("homeAnimation", "slide");
		defaultScreen = sp.getInt("defaultScreen", 3);
		shortcutsAllowed = sp.getBoolean("shortcutsAllowed", false);
		allowedWidgets = sp.getStringSet("allowedWidgets", null);
		wallpaperScrolling = sp.getBoolean("wallpaperScrolling", true);
		homeLblColor = sp.getInt("homeLblColor", -1);
		homescrShadows = sp.getBoolean("homescrShadows", true);
		drawerLblColor = sp.getInt("drawerLblColor", -1);
		drawerscrShadows = sp.getBoolean("drawerscrShadows", true);
		lockDesktop = sp.getBoolean("lockDesktop", false);
		activityCheck = sp.getBoolean("activityCheck", false);
		homePageIndicator = sp.getString("homePageIndicator", "line");
		homeOrientation = sp.getString("homeOrientation", "auto");
		dockBackground = sp.getString("dockBackground", "none");
		locUpdate = sp.getString("locUpdate", "3600000");
		locTrack = sp.getBoolean("locTrack", true);
	}

	public static void setPassword(String pass) {
		passwordHash = SHA.getHash(pass);
		editor.putString("passwordHash", passwordHash);
		editor.commit();
	}

	public static void addToLog(String s) {
		Time t = new Time();
		t.setToNow();
		s += "\n" + t.format("%d.%m.%Y %H:%M") + "\n\n";
		activityLog += s;
		editor.putString("activityLog", activityLog);
		editor.commit();
	}

	public static void clearLog() {
		activityLog = "";
		editor.putString("activityLog", "");
		editor.commit();
	}
}
