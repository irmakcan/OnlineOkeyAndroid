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
