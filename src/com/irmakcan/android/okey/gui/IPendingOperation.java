package com.irmakcan.android.okey.gui;

import com.irmakcan.android.okey.model.Tile;

public interface IPendingOperation {
	// ===========================================================
	// Final Fields
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	public void cancelPendingOperation();
	public void pendingOperationSuccess(Object o);
	public Tile getPendingTile();
}
