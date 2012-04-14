package com.irmakcan.android.okey.model;

import org.andengine.util.color.Color;

public enum TileColor {
	// ===========================================================
	// Elements
	// ===========================================================
	BLACK(0, new Color(0, 0, 0)), BLUE(1, new Color(0, 0, 1f)), ORANGE(2, new Color(0.7f, 0.1f, 0.4f)), RED(3, new Color(1f, 0, 0.4f));
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

//	public static boolean hasPosition(String pPosition){
//		if(pPosition != null){
//			for(Position position : Position.values()){
//				if(pPosition.equalsIgnoreCase(position.mPosition)){
//					return true;
//				}
//			}
//		}
//		return false;
//	}

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
