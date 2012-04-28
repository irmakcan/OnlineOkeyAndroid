package com.irmakcan.android.okey.gson;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.User;

public class JoinRoomResponse extends BaseResponse{
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Position position;
	private List<User> users;
	@SerializedName("play_timeout") private int timeoutInterval;
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public int getTimeoutInterval() {
		return timeoutInterval;
	}
	public void setTimeoutInterval(int timeoutInterval) {
		this.timeoutInterval = timeoutInterval;
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
