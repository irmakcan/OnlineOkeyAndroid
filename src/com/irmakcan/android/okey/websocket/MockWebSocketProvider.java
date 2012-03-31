package com.irmakcan.android.okey.websocket;

import java.net.URI;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketException;

public class MockWebSocketProvider extends WebSocketProvider {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	private static MockWebSocketProvider mInstance;
	// ===========================================================
	// Constructors
	// ===========================================================
	private MockWebSocketProvider() {

	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public static MockWebSocketProvider getInstance(){
		if(mInstance == null){
			mInstance = new MockWebSocketProvider();
		}
		return mInstance;
	}
	
	@Override
	public WebSocket createWebSocketConnection(URI pURI) throws WebSocketException{
		this.mWebSocket = new MockWebSocket();
		return this.mWebSocket;
	}
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
