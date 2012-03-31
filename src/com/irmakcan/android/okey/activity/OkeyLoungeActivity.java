package com.irmakcan.android.okey.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.irmakcan.android.okey.R;
import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.UserPosition;
import com.irmakcan.android.okey.model.Room;
import com.irmakcan.android.okey.model.User;
import com.irmakcan.android.okey.websocket.WebSocketProvider;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

public class OkeyLoungeActivity extends Activity{
	// ===========================================================
	// Constants
	// ===========================================================
	protected static final String LOG_TAG = "OkeyLoungeActivity";
	
	private static final int ROOM_PER_COLUMN = 4;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private Handler mHandler;
	private TableLayout mRoomTableLayout;
	private TextView mPlayerCountView;

	private String mQueuedRoomName;
	
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
		
		this.mRoomTableLayout = (TableLayout)findViewById(R.id.loungescreen_tables);
		
		Button refreshButton = (Button)findViewById(R.id.loungescreen_button_refresh);
		refreshButton.setOnClickListener(mRefreshAction);
		Button createButton = (Button)findViewById(R.id.loungescreen_button_create);
		createButton.setOnClickListener(mCreateAction);
		this.mPlayerCountView = (TextView)findViewById(R.id.loungescreen_textview_player_count);
		
		
		/* -- should be onStart ? -- */
		WebSocket webSocket = WebSocketProvider.getInstance().getWebSocket();
		webSocket.setEventHandler(mWebSocketEventHandler);
		sendRefreshRequest();
		/* ----------- */
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void sendRefreshRequest(){
		WebSocket webSocket = WebSocketProvider.getInstance().getWebSocket();
		try {
			JSONObject json = new JSONObject().put("action", "refresh_list");
			webSocket.send(json.toString());
		} catch (Exception e) {
			// TODO Handle
			e.printStackTrace();
		}
	}
	
	private void sendCreateRoomRequest(String pRoomName){
		WebSocket webSocket = WebSocketProvider.getInstance().getWebSocket();
		try {
			this.mQueuedRoomName = pRoomName;
			JSONObject json = new JSONObject().put("action", "create_room").put("room_name", pRoomName);
			webSocket.send(json.toString());
		} catch (Exception e) {
			// TODO Handle
			e.printStackTrace();
		}
	}
	
	private void sendJoinRoomRequest(String pRoomName){
		WebSocket webSocket = WebSocketProvider.getInstance().getWebSocket();
		try {
			this.mQueuedRoomName = pRoomName;
			JSONObject json = new JSONObject().put("action", "join_room").put("room_name", pRoomName);
			webSocket.send(json.toString());
		} catch (Exception e) {
			// TODO Handle
			e.printStackTrace();
		}
	}
	
	private void generateRoomList(List<Room> pRoomList){
		this.mRoomTableLayout.removeAllViews();
		
		final int rowCount = (pRoomList.size() / ROOM_PER_COLUMN) + 1;
		
		for(int i=0;i < rowCount;i++){
			int start = i*ROOM_PER_COLUMN;
			int end = start + ROOM_PER_COLUMN;
			if(end > pRoomList.size()){
				end = pRoomList.size();
			}
			List<Room> roomSubList = pRoomList.subList(start, end);
			
			TableRow tableRow = new TableRow(this);
	    	for(final Room room : roomSubList){
	    		
	    		Button b = new Button(this);
	    		b.setText(room.getName());
	    		b.setTextSize(18);
		        b.setPadding(6, 5, 15, 5);
		        b.setSingleLine(true);
		        
		        b.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						sendJoinRoomRequest(room.getName());
					}
				});
		        
	    		tableRow.addView(b);
	    	}
	    	tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
	    	this.mRoomTableLayout.addView(tableRow);
		}
	}
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
					final JSONArray roomJSONArray = json.getJSONArray("list");
					final List<Room> roomList = new ArrayList<Room>();
					for(int i=0;i < roomJSONArray.length();i++){
						final JSONObject roomJSON = roomJSONArray.getJSONObject(i);
						final String roomName = roomJSON.getString("room_name");
						final int roomCount = roomJSON.getInt("count");
						roomList.add(new Room(roomName, roomCount));
					}
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mPlayerCountView.setText("Online players: " + playerCount);
							generateRoomList(roomList);
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
				} else if(status.equals("join_room")){
					UserPosition userPosition = UserPosition.fromString(json.getString("position"));
					JSONArray usersJSONArray = json.getJSONArray("users");
					List<User> usersList = new ArrayList<User>();
					for(int i=0;i<usersJSONArray.length();i++){
						JSONObject userJSON = usersJSONArray.getJSONObject(i);
						String name = userJSON.getString("name");
						UserPosition pos = UserPosition.fromString(userJSON.getString("position"));
						usersList.add(new User(name, pos));
					}
					
					Intent i = new Intent(OkeyLoungeActivity.this, OnlineOkeyClientActivity.class);
					i.putExtra("game_information", new GameInformation(mQueuedRoomName, usersList)); // TODO
					Player.getPlayer().setPosition(userPosition);
					startActivity(i);
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
	
	private OnClickListener mRefreshAction = new OnClickListener() {
		@Override
		public void onClick(View v) {
			sendRefreshRequest();
		}
	};
	private OnClickListener mCreateAction = new OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder;
			AlertDialog alertDialog;

			Context context = OkeyLoungeActivity.this;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.room_create_form,
			                               (ViewGroup) findViewById(R.id.room_create_form_root));

			final EditText roomNameEditText = (EditText) layout.findViewById(R.id.room_create_form_edittext_roomname);

			builder = new AlertDialog.Builder(context);
			builder.setTitle("Room Name")
		       .setPositiveButton("Create Room", new DialogInterface.OnClickListener() {
		           @Override
		    	   public void onClick(DialogInterface dialog, int id) {
		        	   OkeyLoungeActivity.this.sendCreateRoomRequest(roomNameEditText.getText().toString());
		           }
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    	   @Override
		    	   public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
			builder.setView(layout);
			alertDialog = builder.create();
			alertDialog.show();
		}
	};
}
