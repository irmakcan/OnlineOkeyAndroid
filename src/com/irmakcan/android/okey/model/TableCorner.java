package com.irmakcan.android.okey.model;

public enum TableCorner {
	// ===========================================================
	// Elements
	// ===========================================================
	SW("sw", Position.WEST, Position.SOUTH), SE("se", Position.SOUTH, Position.EAST),
	NE("ne", Position.EAST, Position.NORTH), NW("nw", Position.NORTH, Position.WEST);
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final String mCorner;
	private final Position mPreviousPosition;
	private final Position mNextPosition;
	// ===========================================================
	// Constructors
	// ===========================================================
	private TableCorner(final String pCorner, final Position pPreviousPosition, final Position pNextPosition) {
		this.mCorner = pCorner;
		this.mPreviousPosition = pPreviousPosition;
		this.mNextPosition = pNextPosition;
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
	public Position nextPosition() {
		return this.mNextPosition;
	}
	public Position previousPosition() {
		return this.mPreviousPosition;
	}
	
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
	
	public static TableCorner nextCornerFromPosition(final Position pPosition){
		if(pPosition != null){
			for(TableCorner corner : TableCorner.values()){
				if(corner.previousPosition() == pPosition){
					return corner;
				}
			}
		}
		throw new IllegalArgumentException("No constant with position " + pPosition + " found");
	}
	
	public static TableCorner previousCornerFromPosition(final Position pPosition){
		if(pPosition != null){
			for(TableCorner corner : TableCorner.values()){
				if(corner.nextPosition() == pPosition){
					return corner;
				}
			}
		}
		throw new IllegalArgumentException("No constant with position " + pPosition + " found");
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
