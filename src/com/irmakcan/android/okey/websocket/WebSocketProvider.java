package com.irmakcan.android.okey.websocket;

import java.net.URI;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketException;

public class WebSocketProvider {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	protected static WebSocket mWebSocket;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	protected WebSocketProvider() {
		
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public static WebSocket createWebSocketConnection(URI pURI) throws WebSocketException {
		try {
			if(mWebSocket != null && mWebSocket.isConnected()){
				mWebSocket.close();
			}
		} catch (WebSocketException e){
			e.printStackTrace(); // Do nothing
		}
		mWebSocket = new WebSocketConnection(pURI);
		return mWebSocket;
	}
	
	public static WebSocket getWebSocket(){
		if(mWebSocket == null){
			throw new IllegalStateException("WebSocket should be created first");
		}
		return mWebSocket;
	}
	
	public static void setWebSocket(final WebSocket pWebSocket){
		mWebSocket = pWebSocket;
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
