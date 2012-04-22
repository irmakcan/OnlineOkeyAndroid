package com.irmakcan.android.okey.websocket;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irmakcan.android.okey.activity.OnlineOkeyClientActivity;
import com.irmakcan.android.okey.gson.BaseResponse;
import com.irmakcan.android.okey.gson.ChatResponse;
import com.irmakcan.android.okey.gson.DrawTileResponse;
import com.irmakcan.android.okey.gson.ErrorResponse;
import com.irmakcan.android.okey.gson.GameStartResponse;
import com.irmakcan.android.okey.gson.ModelDeserializer;
import com.irmakcan.android.okey.gson.NewUserResponse;
import com.irmakcan.android.okey.gson.ThrowTileResponse;
import com.irmakcan.android.okey.gson.UserLeaveResponse;
import com.irmakcan.android.okey.gson.WonResponse;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Tile;

import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

public class OkeyWebSocketEventHandler implements WebSocketEventHandler {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "OkeyWebSocketEventHandler";
	// ===========================================================
	// Fields
	// ===========================================================
	
	private final OnlineOkeyClientActivity mOnlineOkeyClientActivity;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public OkeyWebSocketEventHandler(OnlineOkeyClientActivity pOnlineOkeyClientActivity) {
		this.mOnlineOkeyClientActivity = pOnlineOkeyClientActivity;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	@Override
	public void onOpen() {
		Log.v(LOG_TAG, "OkeyGame WebSocket connected");
		throw new IllegalAccessError("Should not call onOpen in OkeyGame");
	}
	@Override
	public void onMessage(WebSocketMessage message) {
		Log.v(LOG_TAG, "OkeyGame Message received: " + message.getText());
		Gson gson = new Gson();
		BaseResponse baseResponse = gson.fromJson(message.getText(), BaseResponse.class);
		String status = baseResponse.getStatus();
		if(status.equals("error")){
			final ErrorResponse errorResponse = gson.fromJson(message.getText(), ErrorResponse.class);
			this.mOnlineOkeyClientActivity.errorMessage(errorResponse);
		} else if(status.equals("throw_tile")){
			// {"status":"throw_tile","turn":"east","tile":"2:2"}
			gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer())
					.registerTypeAdapter(Tile.class, new ModelDeserializer.TileDeserializer())
					.create();
			final ThrowTileResponse throwTileResponse = gson.fromJson(message.getText(), ThrowTileResponse.class);
			Log.v(LOG_TAG, throwTileResponse.getStatus() + throwTileResponse.getTile().toString() + throwTileResponse.getTurn().toString());
			this.mOnlineOkeyClientActivity.throwTileMessage(throwTileResponse);
		} else if(status.equals("draw_tile")){
			// {"status":"draw_tile","tile":"8:0","turn":"east","center_count":47}
			gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer())
					.registerTypeAdapter(Tile.class, new ModelDeserializer.TileDeserializer())
					.create();
			final DrawTileResponse drawTileResponse = gson.fromJson(message.getText(), DrawTileResponse.class);
			Log.v(LOG_TAG, drawTileResponse.getStatus() + drawTileResponse.getTurn().toString() + drawTileResponse.getCenterCount());
			this.mOnlineOkeyClientActivity.drawTileMessage(drawTileResponse);
		} else if(status.equals("game_start")){
			// {"status":"game_start","turn":"south","center_count":48,"hand":["4:0","7:3",...,"6:2"],"indicator":"4:0"}
			gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer())
					.registerTypeAdapter(Tile.class, new ModelDeserializer.TileDeserializer())
					.create();
			final GameStartResponse gameStartResponse = gson.fromJson(message.getText(), GameStartResponse.class);
			Log.v(LOG_TAG, gameStartResponse.getStatus() + gameStartResponse.getIndicator().toString() + 
					gameStartResponse.getTurn().toString() + gameStartResponse.getCenterCount() + gameStartResponse.getUserHand());
			this.mOnlineOkeyClientActivity.gameStartMessage(gameStartResponse);
		} else if(status.equals("user_won")){
			// {"status":"user_won","turn":user.position,"username":user.username,"hand":[["4:0","7:3"],["4:0","7:3"]]}
			// Show hand
			gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer())
					.registerTypeAdapter(Tile.class, new ModelDeserializer.TileDeserializer())
					.create();
			final WonResponse wonResponse = gson.fromJson(message.getText(), WonResponse.class);
			Log.v(LOG_TAG, wonResponse.getStatus());
//			Log.v(LOG_TAG, wonResponse.getTurn().toString()); // null for tie TODO
//			Log.v(LOG_TAG, wonResponse.getUsername().toString());
//			for(Tile[] group : wonResponse.getHand()){
//				for(Tile t : group){
//					Log.v(LOG_TAG, t.toString());
//				}
//			}
			this.mOnlineOkeyClientActivity.userWonMessage(wonResponse);
		} else if(status.equals("chat")){
			// {"status":"chat","position":"south","message":"Hi, this is a chat message"}
			gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer()).create();
			final ChatResponse chatResponse = gson.fromJson(message.getText(), ChatResponse.class);
			this.mOnlineOkeyClientActivity.chatMessage(chatResponse);
		} else if(status.equals("new_user")){
			// {"status":"new_user","position":"east","username":"irmak"}
			gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer()).create();
			final NewUserResponse newUserResponse = gson.fromJson(message.getText(), NewUserResponse.class);
			this.mOnlineOkeyClientActivity.newUserMessage(newUserResponse);
		} else if(status.equals("user_leave")){
			// {"status":"user_leave","position":"east"}
			gson = new GsonBuilder().registerTypeAdapter(Position.class, new ModelDeserializer.PositionDeserializer()).create();
			final UserLeaveResponse userLeaveResponse = gson.fromJson(message.getText(), UserLeaveResponse.class);
			this.mOnlineOkeyClientActivity.userLeaveMessage(userLeaveResponse);
		}
	}
	@Override
	public void onClose() {
		Log.v(LOG_TAG, "OkeyGame WebSocket disconnected");
	}

}
