package com.irmakcan.android.okey.gson;

import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Tile;

public class ThrowTileResponse extends BaseResponse {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Position turn;
	private Tile tile;
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Position getTurn() {
		return turn;
	}
	public void setTurn(Position turn) {
		this.turn = turn;
	}
	public Tile getTile() {
		return tile;
	}
	public void setTile(Tile tile) {
		this.tile = tile;
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
