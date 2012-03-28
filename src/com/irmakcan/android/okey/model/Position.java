package com.irmakcan.android.okey.model;

public enum Position {
	// ===========================================================
	// Elements
	// ===========================================================
	SOUTH("south"), EAST("east"), NORTH("north"), WEST("west");
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private String mPosition;
	// ===========================================================
	// Constructors
	// ===========================================================
	private Position(String pPosition) {
		this.mPosition = pPosition;
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
	
	public static boolean hasPosition(String pPosition){
		if(pPosition != null){
			for(Position position : Position.values()){
				if(pPosition.equalsIgnoreCase(position.mPosition)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static Position fromString(String pPosition){
		if(pPosition != null){
			for(Position position : Position.values()){
				if(pPosition.equalsIgnoreCase(position.mPosition)){
					return position;
				}
			}
		}
		throw new IllegalArgumentException("No constant with position " + pPosition + " found");
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
