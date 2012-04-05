package com.irmakcan.android.okey.websocket;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class MockWebSocket implements WebSocket {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "MockWebSocket";
	// ===========================================================
	// Fields
	// ===========================================================
	private WebSocketEventHandler mEventHandler;
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
		this.mEventHandler = eventHandler;
		Log.v(LOG_TAG, "setEventHandler called");
	}

	@Override
	public WebSocketEventHandler getEventHandler() {
		Log.v(LOG_TAG, "getEventHandler called");
		return this.mEventHandler;
	}

	@Override
	public void connect() throws WebSocketException {
		Log.v(LOG_TAG, "connect called");
	}

	@Override
	public void send(String data) throws WebSocketException {
		Log.v(LOG_TAG, "send called");
		try{
			JSONObject json = new JSONObject(data);
			final String action = json.getString("action");
			if(action.equals("ready")){
				String response = "{\"status\":\"game_start\",\"turn\":\"south\",\"center_count\":48," +
						"\"hand\":[\"4:0\",\"7:3\",\"6:2\"]," +
						"\"indicator\":\"4:0\"}";
				byte[] resp = response.getBytes("UTF-8");
				Byte[] r = new Byte[resp.length];
				for(int i=0;i<resp.length;i++){
					r[i] = resp[i];
				}
				
				this.mEventHandler.onMessage(new WebSocketMessage(r));
			}
	//		this.mEventHandler.onMessage(message);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws WebSocketException {
		Log.v(LOG_TAG, "close called");
	}

	@Override
	public boolean isConnected() {
		Log.v(LOG_TAG, "isConnected called");
		return true;
	}
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	

}
