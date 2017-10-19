package com.licoforen.parentalcontrollauncher.Adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.licoforen.parentalcontrollauncher.MainActivity;
import com.licoforen.parentalcontrollauncher.CustomViews.ImageCell;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;
import com.licoforen.parentalcontrollauncher.listeners.ClickListener;
import com.licoforen.parentalcontrollauncher.listeners.TouchListener;

public class DrawerAdapter extends BaseAdapter {

	private Context mContext;
	private MainActivity.Pack[] packs;
	private PackageManager pm;
	private int width, height;

	public DrawerAdapter(Context c, MainActivity.Pack packs[],
			PackageManager pm, int w, int h) {
		mContext = c;
		this.packs = packs;
		this.pm = pm;
		width = w;
		height = h;
	}

	public int getCount() {
		return packs.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int pos, View convertView, ViewGroup parent) {
		ImageCell v = null;
		if (convertView == null) {
			v = new ImageCell(mContext, width, height, false, false, 0);
			v.mEmpty = false;
			v.setOnTouchListener(new TouchListener());
			v.setOnClickListener(new ClickListener(mContext, pm));
			if (!ResourceLoader.lockDesktop)
				v.setOnLongClickListener((OnLongClickListener) mContext);
			v.fromDrawer = true;
		} else {
			v = (ImageCell) convertView;
		}

		v.icon.setImageDrawable(packs[pos].icon);
		v.text.setText(packs[pos].label);
		v.packageName = packs[pos].packageName;
		v.name = packs[pos].name;
		v.gridPos = pos;

		return v;
	}
}
