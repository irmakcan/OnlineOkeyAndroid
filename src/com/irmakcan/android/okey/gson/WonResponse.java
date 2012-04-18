package com.irmakcan.android.okey.gson;

import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Tile;

public class WonResponse extends BaseResponse {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Position turn;
	private String username;
	private Tile[][] hand;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Tile[][] getHand() {
		return hand;
	}
	public void setHand(Tile[][] hand) {
		this.hand = hand;
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
