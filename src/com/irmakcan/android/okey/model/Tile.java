package com.irmakcan.android.okey.model;

public class Tile {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private final TileColor mTileColor;
	private final int mValue;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Tile(final TileColor pTileColor, final int pValue) {
		this.mTileColor = pTileColor;
		this.mValue = pValue;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public TileColor getTileColor() {
		return this.mTileColor;
	}
	public int getValue() {
		return this.mValue;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public String toString() {
		return this.getValue() + ":" + this.getTileColor().getColorId();
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof Tile){
			return this.toString().equals(((Tile)o).toString());
		}
		return super.equals(o);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public static Tile fromString(final String pRawTile){
		if(pRawTile != null){
			String[] tileInfo = pRawTile.split(":", 2);
			// May check the val, color limits TODO
			if(tileInfo.length == 2){
				return new Tile(TileColor.fromId(Integer.parseInt(tileInfo[1])), Integer.parseInt(tileInfo[0]));
			}
		}
		throw new IllegalArgumentException("Unexpected raw format: " + pRawTile);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
