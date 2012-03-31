package com.irmakcan.android.okey.model;

public enum UserPosition {
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
	private UserPosition(String pPosition) {
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
			for(UserPosition position : UserPosition.values()){
				if(pPosition.equalsIgnoreCase(position.mPosition)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static UserPosition fromString(String pPosition){
		if(pPosition != null){
			for(UserPosition position : UserPosition.values()){
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
