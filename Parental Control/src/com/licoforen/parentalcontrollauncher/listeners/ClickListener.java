package com.licoforen.parentalcontrollauncher.listeners;

import java.net.URISyntaxException;

import com.licoforen.parentalcontrollauncher.CustomViews.ImageCell;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnClickListener;

public class ClickListener implements OnClickListener {

	Context mContext;
	PackageManager pm;

	public ClickListener(Context c, PackageManager pm) {
		mContext = c;
		this.pm = pm;
	}

	@Override
	public void onClick(View v) {
		ImageCell ic = (ImageCell) v;
		Intent launchIntent = new Intent(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		if (ic.packageName != null) {
			ComponentName cp = new ComponentName(ic.packageName, ic.name);
			launchIntent.setComponent(cp);
		} else {
			try {
				launchIntent = Intent.parseUri(ic.name, 0);
			} catch (URISyntaxException e) {
			}
		}
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME
				| Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		if(launchIntent != null)
			mContext.startActivity(launchIntent);

	}
}
