package com.irmakcan.android.okey.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irmakcan.android.okey.R;
import com.irmakcan.android.okey.gson.BaseResponse;
import com.irmakcan.android.okey.gson.ErrorResponse;
import com.irmakcan.android.okey.gson.JoinLoungeResponse;
import com.irmakcan.android.okey.gson.JoinRoomResponse;
import com.irmakcan.android.okey.gson.LoungeUpdateResponse;
import com.irmakcan.android.okey.gson.ModelDeserializer;
import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Room;
import com.irmakcan.android.okey.model.RoomAdapter;
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
	// ===========================================================
	// Fields
	// ===========================================================
	private Handler mHandler;
	private GridView mGridView;
	private TextView mPlayerCountView;
	private TextView mNoRoomInfo;

	private String mQueuedRoomName;

	private List<Room> mRoomList;
	private RoomAdapter mRoomAdapter;
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

		//		this.mRoomTableLayout = (TableLayout)findViewById(R.id.loungescreen_tables);
		this.mRoomList = new ArrayList<Room>();

		this.mGridView = (GridView)findViewById(R.id.loungescreen_tables_grid);
		this.mRoomAdapter = new RoomAdapter(this, this.mRoomList);
		this.mGridView.setAdapter(this.mRoomAdapter);

		this.mGridView.setOnItemClickListener(mJoinRoomAction);

		Button refreshButton = (Button)findViewById(R.id.loungescreen_button_refresh);
		refreshButton.setOnClickListener(mRefreshAction);
		Button createButton = (Button)findViewById(R.id.loungescreen_button_create);
		createButton.setOnClickListener(mCreateAction);
		this.mPlayerCountView = (TextView)findViewById(R.id.loungescreen_textview_player_count);
		this.mNoRoomInfo = (TextView)findViewById(R.id.loungescreen_textview_noroom_info);

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
		if(!WebSocketProvider.getWebSocket().isConnected()){
			this.finish(); // TODO test
		}
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
			this.finish();
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
			this.finish();
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
			this.finish();
			e.printStackTrace();
		}
	}

	private void generateRoomList(List<Room> pRoomList){
		this.mRoomList.clear();
		this.mRoomList.addAll(pRoomList);
		this.mRoomAdapter.notifyDataSetChanged();
		this.mGridView.invalidateViews();
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
				gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer()).create();
				final LoungeUpdateResponse loungeUpdate = gson.fromJson(message.getText(), LoungeUpdateResponse.class);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mPlayerCountView.setText("Online players: " + loungeUpdate.getPlayerCount());
						if(loungeUpdate.getRoomList().size() <= 0){
							mNoRoomInfo.setVisibility(TextView.VISIBLE);
							mGridView.setVisibility(GridView.GONE);
						}else{
							mNoRoomInfo.setVisibility(TextView.GONE);
							mGridView.setVisibility(GridView.VISIBLE);
							generateRoomList(loungeUpdate.getRoomList());
						}
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
				i.putExtra("game_information", new GameInformation(mQueuedRoomName, joinRoomResponse.getUsers(), joinRoomResponse.getTimeoutInterval())); // TODO
				Player.getPlayer().setPosition(joinRoomResponse.getPosition());
				startActivity(i);
			}else if(status.equals("join_lounge")){
				JoinLoungeResponse joinLoungeResponse = gson.fromJson(message.getText(), JoinLoungeResponse.class);
				Player.getPlayer().setPoints(joinLoungeResponse.getPoints());
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
			final AlertDialog alertDialog;

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
			alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			alertDialog.setOnShowListener(new OnShowListener() { // Force show soft keyboard 
				@Override
				public void onShow(DialogInterface dialog) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				}
			});
			alertDialog.show();

			roomNameEditText.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
						// Dissmiss keyboard
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// Simulate Send button click
						alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
						return true;
					}
					return false;
				}
			});
		}
	};
	private OnItemClickListener mJoinRoomAction = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Room room = (Room) mRoomAdapter.getItem(position);
			sendJoinRoomRequest(room.getName());
		}
	};
}
