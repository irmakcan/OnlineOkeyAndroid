package com.irmakcan.android.okey.model;

public class Room {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private String mName;
	private int mPlayerCount;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Room(String pName, int pPlayerCount) {
		this.mName = pName;
		this.mPlayerCount = pPlayerCount;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public int getPlayerCount() {
		return mPlayerCount;
	}

	public void setPlayerCount(int pPlayerCount) {
		this.mPlayerCount = pPlayerCount;
	}

	public String getName() {
		return this.mName;
	}

	public void setName(String pName) {
		this.mName = pName;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	
	
	

	
}
