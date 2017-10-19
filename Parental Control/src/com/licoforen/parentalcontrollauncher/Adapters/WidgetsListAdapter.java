package com.licoforen.parentalcontrollauncher.Adapters;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WidgetsListAdapter extends ArrayAdapter<AppWidgetProviderInfo> {

	private Context mContext;
	private AppWidgetProviderInfo widgets[];
	private int height;

	public WidgetsListAdapter(Context context, int resource,
			AppWidgetProviderInfo widgets[], int height) {
		super(context, resource);
		mContext = context;
		this.widgets = widgets;
		this.height = height;
	}

	@Override
	public int getCount() {
		return widgets.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PackageManager pm = mContext.getPackageManager();
		TextView v = null;
		if (convertView == null) {
			v = new TextView(mContext);
			v.setText(widgets[position].label);
			v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			v.setGravity(Gravity.CENTER_VERTICAL);
			v.setHeight(height);
			v.setPadding(15, 0, 15, 0);
			Drawable d = pm.getDrawable(
					widgets[position].provider.getPackageName(),
					widgets[position].icon, null);
			d.setBounds(5, 5, height - 5, height - 5);
			v.setCompoundDrawables(d, null, null, null);
			v.setCompoundDrawablePadding(height / 6);
		} else {
			v = (TextView) convertView;
			v.setText(widgets[position].label);
			Drawable d = pm.getDrawable(
					widgets[position].provider.getPackageName(),
					widgets[position].icon, null);
			d.setBounds(5, 5, height - 5, height - 5);
			v.setCompoundDrawables(d, null, null, null);
		}
		return v;
	}

}
