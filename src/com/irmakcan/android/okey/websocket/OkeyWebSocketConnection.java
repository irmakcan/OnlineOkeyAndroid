package com.irmakcan.android.okey.websocket;

import java.net.URI;

import android.util.Log;

import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketException;

public class OkeyWebSocketConnection extends WebSocketConnection{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "OkeyWebSocketConnection";
	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public OkeyWebSocketConnection(URI url, String protocol) throws WebSocketException {
		super(url, protocol);
	}

	public OkeyWebSocketConnection(URI url) throws WebSocketException {
		super(url);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public synchronized void send(String data) throws WebSocketException {
		super.send(data);
		Log.v(LOG_TAG, "Message Sent: " + data);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
