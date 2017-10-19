package com.licoforen.parentalcontrollauncher.Interfaces;

import android.content.ClipData;

public interface DragSource {

	public boolean allowDrag();

	public ClipData clipDataForDragDrop();

	public void onDragStarted();

	public void onDropCompleted(DropTarget target, boolean success);

}
