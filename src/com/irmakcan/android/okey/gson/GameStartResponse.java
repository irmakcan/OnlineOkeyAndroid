package com.irmakcan.android.okey.gson;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Tile;

public class GameStartResponse extends BaseResponse {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Position turn;
	@SerializedName("center_count") private int centerCount;
	@SerializedName("hand") private List<Tile> userHand;
	private Tile indicator;
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
	public int getCenterCount() {
		return centerCount;
	}
	public void setCenterCount(int centerCount) {
		this.centerCount = centerCount;
	}
	public List<Tile> getUserHand() {
		return userHand;
	}
	public void setUserHand(List<Tile> userHand) {
		this.userHand = userHand;
	}
	public Tile getIndicator() {
		return indicator;
	}
	public void setIndicator(Tile indicator) {
		this.indicator = indicator;
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
