package com.irmakcan.android.okey.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.entity.primitive.Rectangle;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.irmakcan.android.okey.gui.Board;
import com.irmakcan.android.okey.gui.BoardFragment;
import com.irmakcan.android.okey.gui.CornerTileStackRectangle;
import com.irmakcan.android.okey.gui.IPendingOperation;
import com.irmakcan.android.okey.gui.TileCountText;
import com.irmakcan.android.okey.gui.TileSprite;
import com.irmakcan.android.okey.gui.UserInfoArea;
import com.irmakcan.android.okey.websocket.WebSocketProvider;


public class TableManager implements IPendingOperation {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "Table Manager: ";
	// ===========================================================
	// Fields
	// ===========================================================

	private final Map<Position, User> mUsers;
	private final Map<Position, UserInfoArea> mUserAreas;
	private TileCountText mTileCountText;

	private Map<TableCorner, CornerTileStackRectangle> mCorners;
	private Tile mIndicator;

	private int mCenterCount;
	private final Board mBoard;
	private final Position mPosition;
	private final Rectangle mCenterArea;
	private final int mTimeoutInterval;

	private IPendingOperation mIPendingOperation;

	private Position mTurn;
	// ===========================================================
	// Constructors
	// ===========================================================
	public TableManager(final Position pPosition, final Board pBoard, 
			final Map<TableCorner, CornerTileStackRectangle> pCorners, final Rectangle pCenterArea,
			final Map<Position, UserInfoArea> pUserAreas, final TileCountText pTileCountText, final int pTimeoutInterval) {
		this.mBoard = pBoard;
		this.mCorners = pCorners;
		this.mPosition = pPosition;
		this.mCenterArea = pCenterArea;
		this.mUserAreas = pUserAreas;
		this.mTileCountText = pTileCountText;
		this.mTimeoutInterval = pTimeoutInterval;
		this.mUsers = new HashMap<Position, User>();
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Board getBoard() {
		return this.mBoard;
	}
	public Position getUserPosition() {
		return this.mPosition;
	}
	public CornerTileStackRectangle getNextCornerStack(){
		return mCorners.get(TableCorner.nextCornerFromPosition(this.mPosition));
	}
	public CornerTileStackRectangle getPreviousCornerStack(){
		return mCorners.get(TableCorner.previousCornerFromPosition(this.mPosition));
	}

	public Tile getIndicator() {
		return this.mIndicator;
	}
	public void setIndicator(Tile pIndicator) {
		this.mIndicator = pIndicator;
	}
	public Position getTurn() {
		return this.mTurn;
	}
	public void setTurn(Position pTurn) {
		if(this.mTurn != null){ // Clear the previous player
			this.mUserAreas.get(this.mTurn).setEnabled(false);
			this.mUserAreas.get(this.mTurn).stopTimer();
		}
		this.mUserAreas.get(pTurn).setEnabled(true);
		this.mUserAreas.get(pTurn).startTimer(this.mTimeoutInterval);
		this.mTurn = pTurn;
	}
	public Rectangle getCenterArea(){
		return this.mCenterArea;
	}
	public int getCenterCount() {
		return this.mCenterCount;
	}
	public void setCenterCount(int pCenterCount) {
		mTileCountText.setCount(pCenterCount);
		this.mCenterCount = pCenterCount;
	}
	public CornerTileStackRectangle getCornerStack(final TableCorner pTableCorner){
		return mCorners.get(pTableCorner);
	}

	public User getUserAt(Position pPosition){
		return mUsers.get(pPosition);
	}
	public void setUserAt(Position pPosition, User pUser){
		mUsers.put(pPosition, pUser);
		mUserAreas.get(pPosition).setTextFrom(pUser);
	}
	public void clearUserAt(Position pPosition){
		mUserAreas.get(pPosition).setTextFrom(null);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void cancelPendingOperation() {
		if(this.mIPendingOperation != null){
			this.mIPendingOperation.cancelPendingOperation();
			this.mIPendingOperation = null;
		}
	}
	@Override
	public void pendingOperationSuccess(Object o) {
		if(this.mIPendingOperation != null){
			this.mIPendingOperation.pendingOperationSuccess(o);
			this.mIPendingOperation = null;
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================

	public void forceDrawCenter(final TileSprite pTileSprite){
		this.mBoard.addChild(pTileSprite);
	}
	public void forceDrawLeft(){
		final TileSprite ts = this.mCorners.get(TableCorner.previousCornerFromPosition(this.mPosition)).pop();
		this.mBoard.addChild(ts);
	}
	public void forceThrow(Tile tile) {
		TileSprite tileSprite = this.mBoard.getFirstTileSprite(tile);
		tileSprite.getIHolder().removeTileSprite();
		tileSprite.disableTouch();
		this.mCorners.get(TableCorner.nextCornerFromPosition(this.mPosition)).push(tileSprite);
	}

	public synchronized boolean setPendingOperation(final IPendingOperation pIPendingOperation){
		if(this.mIPendingOperation != null){
			return false;
		}
		this.mIPendingOperation = pIPendingOperation;
		return true;
	}
	public synchronized IPendingOperation getPendingOperation(){
		return this.mIPendingOperation;
	}

	public void drawCenterTile(IPendingOperation pIPendingOperation) {
		if(!this.setPendingOperation(pIPendingOperation)){
			pIPendingOperation.cancelPendingOperation();
			return;
		}
		if(this.mTurn == this.mPosition){
			try {
				JSONObject json = new JSONObject().put("action", "draw_tile").put("center", true);
				WebSocketProvider.getWebSocket().send(json.toString());
			} catch (Exception e){
				e.printStackTrace();
				this.cancelPendingOperation();
			}
		} else {
			this.cancelPendingOperation();
		}
	}
	public void drawCornerTile(IPendingOperation pIPendingOperation) {
		if(!this.setPendingOperation(pIPendingOperation)){
			pIPendingOperation.cancelPendingOperation();
			return;
		}
		if(this.mTurn == this.mPosition){
			try {
				JSONObject json = new JSONObject().put("action", "draw_tile").put("center", false);
				WebSocketProvider.getWebSocket().send(json.toString());
			} catch (Exception e){
				e.printStackTrace();
				this.cancelPendingOperation();
			}
		} else {
			this.cancelPendingOperation();
		}
	}
	public void throwTile(final IPendingOperation pIPendingOperation, final Tile pTile) {
		if(!this.setPendingOperation(pIPendingOperation)){
			pIPendingOperation.cancelPendingOperation();
			return;
		}
		Log.v(LOG_TAG, "Turn: " + this.getTurn().toString() + " Pos: " + this.getUserPosition());
		if(this.getTurn() == this.getUserPosition()){
			try {
				JSONObject json = new JSONObject().put("action", "throw_tile").put("tile", pTile.toString());
				WebSocketProvider.getWebSocket().send(json.toString());
			} catch (Exception e){
				e.printStackTrace();
				this.cancelPendingOperation();
			}
		} else {
			this.cancelPendingOperation();
		}
	}
	public void throwTileToFinish(final IPendingOperation pIPendingOperation, final Tile pTile) {
		if(!this.setPendingOperation(pIPendingOperation)){
			pIPendingOperation.cancelPendingOperation();
			return;
		}
		Log.v(LOG_TAG, "Turn: " + this.getTurn().toString() + " Pos: " + this.getUserPosition());
		if(this.getTurn() == this.getUserPosition()){
			try {
				JSONArray hand = new JSONArray();
				List<List<Tile>> tileList = this.mBoard.getHandWithoutTile(pTile);
				for(List<Tile> tileGroup : tileList){ //TODO
					JSONArray group = new JSONArray();
					for(Tile tile : tileGroup){
						group.put(tile.toString());
					}
					hand.put(group);
				}

				JSONObject json = new JSONObject().put("action", "throw_to_finish")
						.put("hand", hand).put("tile", pTile.toString());
				Log.v(LOG_TAG, "Sending data: " + json.toString());//TODO
				WebSocketProvider.getWebSocket().send(json.toString());
			} catch (Exception e){
				e.printStackTrace();
				this.cancelPendingOperation();
			}
		} else {
			this.cancelPendingOperation();
		}
	}
	public void moveTile(final IPendingOperation pIPendingOperation, final TileSprite pTileSprite, final float pX, final float pY) {
		if(this.mBoard.isEmpty(pX, pY)){
			BoardFragment bf = this.mBoard.addChild(pTileSprite, pX, pY);
			if(bf != null){
				pIPendingOperation.pendingOperationSuccess(bf);
			}else{
				pIPendingOperation.cancelPendingOperation();
			}
		}else{
			pIPendingOperation.cancelPendingOperation();
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
