package com.irmakcan.android.okey.model;

import org.andengine.util.color.Color;

public enum TileColor {
	// ===========================================================
	// Elements
	// ===========================================================
	BLACK(0, new Color(0, 0, 0)), BLUE(1, new Color(0.094f, 0.094f, 0.737f)), ORANGE(2, new Color(1f, 0.459f, 0.02f)), RED(3, new Color(0.776f, 0.051f, 0.173f));
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final int mColorId;
	private final Color mColor;
	// ===========================================================
	// Constructors
	// ===========================================================
	private TileColor(final int pColorId, final Color pColor) {
		this.mColorId = pColorId;
		this.mColor = pColor;
		
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Color getColor(){
		return this.mColor;
	}
	public int getColorId() {
		return this.mColorId;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static TileColor fromId(final int pColorId){
		for(TileColor color : TileColor.values()){
			if(pColorId == color.mColorId){
				return color;
			}
		}
		throw new IllegalArgumentException("No constant with color id " + pColorId + " found");
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
