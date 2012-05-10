package com.irmakcan.android.okey.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class User implements Serializable {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final long serialVersionUID = 1L;
	// ===========================================================
	// Fields
	// ===========================================================
	@SerializedName("name") private String mUserName;
	@SerializedName("position") private Position mPosition;
	@SerializedName("points") private int mPoints;
	// ===========================================================
	// Constructors
	// ===========================================================
	public User() {
		// TODO Auto-generated constructor stub
	}
	public User(String pUserName){
		this.mUserName = pUserName;
	}
	public User(String pUserName, Position pPosition) {
		this(pUserName);
		this.mPosition = pPosition;
	}
	public User(String pUserName, Position pPosition, int pPoints) {
		this(pUserName, pPosition);
		this.mPoints = pPoints;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public String getUserName() {
		return this.mUserName;
	}
	public void setUserName(String pUserName) {
		this.mUserName = pUserName;
	}
	public Position getPosition() {
		return this.mPosition;
	}
	public void setPosition(Position pPosition) {
		this.mPosition = pPosition;
	}
	public int getPoints() {
		return this.mPoints;
	}
	public void setPoints(int pPoints) {
		this.mPoints = pPoints;
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
