package com.irmakcan.android.okey.model;

import java.io.Serializable;
import java.util.List;

public class GameInformation implements Serializable{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final long serialVersionUID = 1L;
	// ===========================================================
	// Fields
	// ===========================================================
	
	private List<User> mUserList;
	private String mTableName;
	private int mTimeoutInterval;
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public GameInformation() {
		// TODO Auto-generated constructor stub
	}
	public GameInformation(String pTableName, List<User> pUserList, int pTimeoutInterval) {
		this.mUserList = pUserList;
		this.mTableName = pTableName; 
		this.mTimeoutInterval = pTimeoutInterval;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public List<User> getUserList() {
		return this.mUserList;
	}

	public void setUserList(List<User> pUserList) {
		this.mUserList = pUserList;
	}

	public String getTableName() {
		return this.mTableName;
	}

	public void setTableName(String pTableName) {
		this.mTableName = pTableName;
	}
	
	public int getTimeoutInterval() {
		return this.mTimeoutInterval;
	}
	public void setTimeoutInterval(int pTimeoutInterval) {
		this.mTimeoutInterval = pTimeoutInterval;
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
