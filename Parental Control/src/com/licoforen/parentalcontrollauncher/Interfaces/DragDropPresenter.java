package com.licoforen.parentalcontrollauncher.Interfaces;

public interface DragDropPresenter {

public boolean isDragDropEnabled ();

public void onDragStarted (DragSource source);

public void onDropCompleted (DropTarget target, DragSource source, boolean success);

}
