package com.irmakcan.android.okey.activity;

import java.util.List;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irmakcan.android.okey.R;
import com.irmakcan.android.okey.gson.BaseResponse;
import com.irmakcan.android.okey.gson.ErrorResponse;
import com.irmakcan.android.okey.gson.JoinRoomResponse;
import com.irmakcan.android.okey.gson.LoungeUpdateResponse;
import com.irmakcan.android.okey.gson.ModelDeserializer;
import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Room;
import com.irmakcan.android.okey.model.User;
import com.irmakcan.android.okey.websocket.WebSocketProvider;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
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

	}

	@Override
	protected void onStart() {
		super.onStart();
		WebSocket webSocket = WebSocketProvider.getWebSocket();
		webSocket.setEventHandler(mWebSocketEventHandler);
		sendRefreshRequest();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			WebSocketProvider.getWebSocket().close();
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void sendRefreshRequest(){
		WebSocket webSocket = WebSocketProvider.getWebSocket();
		try {
			JSONObject json = new JSONObject().put("action", "refresh_list");
			webSocket.send(json.toString());
		} catch (Exception e) {
			// TODO Handle
			e.printStackTrace();
		}
	}

	private void sendCreateRoomRequest(String pRoomName){
		WebSocket webSocket = WebSocketProvider.getWebSocket();
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
		WebSocket webSocket = WebSocketProvider.getWebSocket();
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
			Gson gson = new Gson();
			BaseResponse baseResponse = gson.fromJson(message.getText(), BaseResponse.class);
			String status = baseResponse.getStatus();
			if(status.equals("lounge_update")){ //{"status":"lounge_update","player_count":5,"list":[{"room_name":"room3","count":1}]}
				final LoungeUpdateResponse loungeUpdate = gson.fromJson(message.getText(), LoungeUpdateResponse.class);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mPlayerCountView.setText("Online players: " + loungeUpdate.getPlayerCount());
						generateRoomList(loungeUpdate.getRoomList());
					}
				});
			}else if(status.equals("error")){
				final ErrorResponse errorResponse = gson.fromJson(message.getText(), ErrorResponse.class);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(OkeyLoungeActivity.this.getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			}else if(status.equals("join_room")){ // {"status":"join_room","position":"east","users":[{"name":"irmak4","position":"south"}]}
				gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer()).create();
				final JoinRoomResponse joinRoomResponse = gson.fromJson(message.getText(), JoinRoomResponse.class);
				Log.v(LOG_TAG, "Pos: " + joinRoomResponse.getPosition());
				for(User u : joinRoomResponse.getUsers()){
					Log.v(LOG_TAG, "Name: " + u.getUserName() + "Pos: " + u.getPosition());
				}

				Intent i = new Intent(OkeyLoungeActivity.this, OnlineOkeyClientActivity.class);
				i.putExtra("game_information", new GameInformation(mQueuedRoomName, joinRoomResponse.getUsers())); // TODO
				Player.getPlayer().setPosition(joinRoomResponse.getPosition());
				startActivity(i);
			}else{
				// TODO
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
