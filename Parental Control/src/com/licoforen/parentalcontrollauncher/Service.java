package com.licoforen.parentalcontrollauncher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;
import com.licoforen.parentalcontrollauncher.listeners.LocationTracker;

public class Service extends IntentService {

	String lastChecked = null;

	private PendingIntent locIntent;
	private AlarmManager alarmManager;
	private int oldInterval = 0;

	public Service() {
		super("ParentalControlService");
	}

	@SuppressLint("InflateParams")
	@Override
	protected void onHandleIntent(Intent intent) {
		String name, packagename;
		List<RunningTaskInfo> runningApps;
		ActivityManager am = (ActivityManager) getApplicationContext()
				.getSystemService(Activity.ACTIVITY_SERVICE);

		Set<String> alwdPkgs = new HashSet<String>();
		alwdPkgs.add("com.android.systemui");
		alwdPkgs.add("com.android.phone");
		alwdPkgs.add("com.android.packageinstaller");
		alwdPkgs.add("com.licoforen.parentalcontrollauncher");
		alwdPkgs.add("android");

		Set<String> alwdActy = new HashSet<String>();
		alwdActy.add("com.android.settings.AllowBindAppWidgetActivity");
		alwdActy.add("com.android.settings.ActivityPicker");

		while (true) {

			if (ResourceLoader.activityCheck) {
				runningApps = am.getRunningTasks(1);
				packagename = runningApps.get(0).topActivity.getPackageName();
				name = runningApps.get(0).topActivity.getClassName();
				if (ResourceLoader.allowedApps != null
						&& !ResourceLoader.allowedApps.contains(packagename)
						&& !ResourceLoader.allowedWidgets.contains(name)
						&& !alwdPkgs.contains(packagename)
						&& !alwdActy.contains(name)
						&& !packagename.equals(lastChecked)) {

					Intent lockIntent = new Intent(getApplicationContext(),
							LockScreen.class);
					lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
							| Intent.FLAG_ACTIVITY_NO_HISTORY
							| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
					lockIntent.putExtra("AppLocked", new ComponentName(
							packagename, name));
					getApplicationContext().startActivity(lockIntent);
					lastChecked = packagename;
				}
				if (lastChecked!=null && !packagename.equals(lastChecked)
						&& !name.equals("com.licoforen.parentalcontrollauncher.LockScreen")) {
					lastChecked = null;
				}
			}

			setLocTracking();

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

	private void setLocTracking() {
		locIntent = PendingIntent.getBroadcast(this, 0, new Intent(this,
				LocationTracker.class), 0);
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		int curInterval = Integer.parseInt(ResourceLoader.locUpdate);
		if (ResourceLoader.locTrack && curInterval != oldInterval) {
			oldInterval = curInterval;
			alarmManager.cancel(locIntent);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), curInterval, locIntent);
		} else if (!ResourceLoader.locTrack) {
			alarmManager.cancel(locIntent);
		}
	}
}
