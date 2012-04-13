package com.irmakcan.android.okey.gui;

public interface IHolder {
	// ===========================================================
	// Final Fields
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	public void addTileSprite(final TileSprite pTileSprite);
	public TileSprite getTileSprite();
	public TileSprite removeTileSprite();
	public boolean hasTileSprite();
}
