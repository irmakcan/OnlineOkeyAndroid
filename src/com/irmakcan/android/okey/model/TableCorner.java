package com.irmakcan.android.okey.model;

public enum TableCorner {
	// ===========================================================
	// Elements
	// ===========================================================
	SW("sw"), SE("se"), NW("nw"), NE("ne");
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private String mCorner;
	// ===========================================================
	// Constructors
	// ===========================================================
	private TableCorner(final String pCorner) {
		this.mCorner = pCorner;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static TableCorner fromString(String pCorner){
		if(pCorner != null){
			for(TableCorner corner : TableCorner.values()){
				if(pCorner.equalsIgnoreCase(corner.mCorner)){
					return corner;
				}
			}
		}
		throw new IllegalArgumentException("No constant with corner " + pCorner + " found");
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
