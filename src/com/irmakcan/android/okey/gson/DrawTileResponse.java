package com.irmakcan.android.okey.gson;

import com.google.gson.annotations.SerializedName;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Tile;

public class DrawTileResponse extends BaseResponse{
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Position turn;
	private Tile tile;
	@SerializedName("center_count") private int centerCount;
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
	public int getCenterCount() {
		return centerCount;
	}
	public void setCenterCount(int centerCount) {
		this.centerCount = centerCount;
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
