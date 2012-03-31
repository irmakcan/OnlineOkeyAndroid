package com.irmakcan.android.okey.websocket;

import android.util.Log;
import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;

public class MockWebSocket implements WebSocket {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "MockWebSocket";
	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void setEventHandler(WebSocketEventHandler eventHandler) {
		Log.v(LOG_TAG, "setEventHandler called");
	}

	@Override
	public WebSocketEventHandler getEventHandler() {
		Log.v(LOG_TAG, "getEventHandler called");
		return null;
	}

	@Override
	public void connect() throws WebSocketException {
		Log.v(LOG_TAG, "connect called");
	}

	@Override
	public void send(String data) throws WebSocketException {
		Log.v(LOG_TAG, "send called");
	}

	@Override
	public void close() throws WebSocketException {
		Log.v(LOG_TAG, "close called");
	}

	@Override
	public boolean isConnected() {
		Log.v(LOG_TAG, "isConnected called");
		return false;
	}
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	

}
