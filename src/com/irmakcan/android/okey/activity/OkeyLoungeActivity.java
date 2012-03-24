package com.irmakcan.android.okey.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.irmakcan.android.okey.R;
import com.irmakcan.android.okey.websocket.WebSocketProvider;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

public class OkeyLoungeActivity extends Activity{
	// ===========================================================
	// Constants
	// ===========================================================
	protected static final String LOG_TAG = "OkeyLoungeActivity";
	// ===========================================================
	// Fields
	// ===========================================================
	
	private Handler mHandler;
	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lounge_screen);
		
		mHandler = new Handler();
		
		WebSocket webSocket = WebSocketProvider.getInstance().getWebSocket();
		webSocket.setEventHandler(mWebSocketEventHandler);
		try {
			JSONObject json = new JSONObject().put("action", "refresh_list");
			webSocket.send(json.toString());
		} catch (Exception e) {
			// TODO Handle
			e.printStackTrace();
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private WebSocketEventHandler mWebSocketEventHandler = new WebSocketEventHandler() {
		@Override
		public void onOpen() {
			Log.v(LOG_TAG, "Lounge WebSocket connected");
			throw new IllegalAccessError("Should not call onOpen in lounge");
		}
		@Override
		public void onMessage(WebSocketMessage message) {
			Log.v(LOG_TAG, "Lounge Message received: " + message.getText());
			try {
				Log.v(LOG_TAG, message.getText());
				final JSONObject json = new JSONObject(message.getText());
				final String status =json.getString("status");
				if(status.equals("lounge_update")){
					Log.v(LOG_TAG, "auth success");
					final int playerCount = json.getInt("player_count");
					final JSONArray roomList = json.getJSONArray("list");
//					for(int i=0;i < roomList.length();i++){
//						final JSONObject room = roomList.getJSONObject(i);
//					}
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(OkeyLoungeActivity.this.getApplicationContext(), "Player Count: " + playerCount + " List: " + roomList, Toast.LENGTH_LONG).show();
						}
					});
					
				} else if(status.equals("error")){
					final String errorMessage = json.getString("message");
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(OkeyLoungeActivity.this.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					// TODO
				}
			} catch (JSONException e) {
				// Messaging error TODO
				e.printStackTrace();
			}
		}
		@Override
		public void onClose() {
			Log.v(LOG_TAG, "Lounge WebSocket disconnected");
		}
	};
}
