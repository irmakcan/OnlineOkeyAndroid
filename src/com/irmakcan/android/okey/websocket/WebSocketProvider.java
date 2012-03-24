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
	private static WebSocketProvider mInstance;
	
	private WebSocket mWebSocket;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	private WebSocketProvider() {
		
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public static WebSocketProvider getInstance(){
		if(mInstance == null){
			mInstance = new WebSocketProvider();
		}
		return mInstance;
	}
	
	public WebSocket createWebSocketConnection(URI pURI) throws WebSocketException{
		if(this.mWebSocket != null && this.mWebSocket.isConnected()){
			this.mWebSocket.close();
		}
		this.mWebSocket = new WebSocketConnection(pURI);
		return this.mWebSocket;
	}
	
	public WebSocket getWebSocket(){
		return this.mWebSocket;
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
