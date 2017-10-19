package com.licoforen.parentalcontrollauncher.Interfaces;

public interface DropTarget {

	public boolean allowDrop(DragSource source);

	public void onDrop(DragSource source);

	public void onDragEnter(DragSource source);

	public void onDragExit(DragSource source);

}
