package com.licoforen.framework.implementation;

import android.graphics.Bitmap;

import com.licoforen.framework.Graphics.PixmapFormat;
import com.licoforen.framework.Pixmap;

public class AndroidPixmap implements Pixmap {
	Bitmap bitmap;
	PixmapFormat format;

	public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}
	
	public int getWidth() {
		return bitmap.getWidth();
	}
	
	public int getHeight() {
		return bitmap.getHeight();
	}
	
	public PixmapFormat getFormat() {
		return format;
	}
	
	public void dispose() {
		bitmap.recycle();
	}
}
