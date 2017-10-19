package com.licoforen.parentalcontrollauncher.CustomViews;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.licoforen.parentalcontrollauncher.R;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;
import com.licoforen.parentalcontrollauncher.Interfaces.DragSource;
import com.licoforen.parentalcontrollauncher.Interfaces.DropTarget;

public class ImageCell extends LinearLayout implements DragSource, DropTarget {

	public TextView text = new TextView(getContext());
	public ImageView icon = new ImageView(getContext());
	public String packageName, name;
	public boolean fromDrawer = false, isInDock = false, mEmpty = true;
	public int gridPos, widgetID = -1, spanX, spanY;
	private float scale;

	public ImageCell(Context context, int width, int height, boolean inDock,
			boolean inHome, int padding) {
		super(context);

		scale = getResources().getDisplayMetrics().density;

		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		setPadding(padding, padding, padding, padding);

		int wh = width > 100 ? 70 : 50;
		if (inDock) {
			addView(icon, width, height);
			icon.setPadding(padding, padding, padding, padding);
			isInDock = true;
		} else {
			addView(icon, wh, wh);
			addView(text, width - dp(2 * padding), height - wh
					- dp(2 * padding));
			
			text.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
			if (ResourceLoader.homescrShadows)
				text.setShadowLayer(1.5f, 1.5f, 1.5f, Color.BLACK);
			text.setEllipsize(TextUtils.TruncateAt.END);
		}

		if (inHome) {
			text.setSingleLine();
			text.setTextAppearance(getContext(), R.style.HomeTextStyle);
			text.setTextColor(ResourceLoader.homeLblColor);
			if (wh == dp(70))
				text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
			else
				text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
		} else if (!inDock) {
			text.setMaxLines(2);
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			text.setTextColor(ResourceLoader.drawerLblColor);
			if (!ResourceLoader.drawerscrShadows
					&& ResourceLoader.homescrShadows)
				text.setShadowLayer(0, 0, 0, -1);
		}
	}

	public boolean allowDrag() {
		return !mEmpty;
	}

	public ClipData clipDataForDragDrop() {
		return null;
	}

	public Drawable getIcon() {
		return icon.getDrawable();
	}

	public CharSequence getText() {
		return text.getText();
	}

	public void onDragStarted() {
		invalidate();

		if (!fromDrawer) {
			icon.setAlpha(0f);
			text.setAlpha(0f);
		}
	}

	public void onDropCompleted(DropTarget target, boolean success) {
		invalidate();

		if (success) {
			if (!fromDrawer) {
				icon.setImageDrawable(null);
				text.setText("");
				setOnClickListener(null);
				setOnLongClickListener(null);
				mEmpty = true;
			}
		} else {
			icon.setAlpha(1f);
			text.setAlpha(1f);
		}

	}

	public boolean allowDrop(DragSource source) {

		if (source == this)
			return false;

		return mEmpty;
	}

	public void onDrop(DragSource source) {

		try {
			ImageCell s = (ImageCell) source;
			icon.setImageDrawable(s.icon.getDrawable());
			icon.setAlpha(1f);
			text.setAlpha(1f);
			if (ResourceLoader.homescrlabels)
				text.setText(s.text.getText());
			packageName = s.packageName;
			name = s.name;
			if (this != s)
				s.name = s.packageName = null;
			mEmpty = false;

			this.invalidate();
		} catch (Exception e) {
		}

	}

	public void onDragEnter(DragSource source) {
		try {
			ImageCell ic = (ImageCell) source;
			icon.setImageDrawable(ic.icon.getDrawable());
			icon.setAlpha(0.4f);
		} catch (Exception e) {
		}
	}

	public void onDragExit(DragSource source) {
		try {
			if (this != (ImageCell) source) {
				icon.setImageDrawable(null);
			}
			icon.setAlpha(0f);
		} catch (Exception e) {
		}
	}

	public boolean isEmpty() {
		return mEmpty;
	}

	public boolean performClick() {
		if (!mEmpty)
			return super.performClick();
		return false;
	}

	public boolean performLongClick() {
		if (!mEmpty)
			return super.performLongClick();
		return false;
	}

	int dp(int d) {
		return (int) (d * scale + 0.5f);
	}
}
