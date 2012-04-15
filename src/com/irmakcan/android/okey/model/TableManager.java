package com.irmakcan.android.okey.model;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.irmakcan.android.okey.gui.Board;
import com.irmakcan.android.okey.gui.BoardFragment;
import com.irmakcan.android.okey.gui.CornerTileStackRectangle;
import com.irmakcan.android.okey.gui.IPendingOperation;
import com.irmakcan.android.okey.gui.TileSprite;
import com.irmakcan.android.okey.websocket.WebSocketProvider;


public class TableManager implements IPendingOperation {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private Map<TableCorner, CornerTileStackRectangle> mCorners;
	private Tile mIndicator;

	private int mCenterCount;
	private final Board mBoard;
	private final Position mPosition;
	
	private IPendingOperation mIPendingOperation;
	
	private Position mTurn;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public TableManager(final Position pPosition, final Board pBoard, final Map<TableCorner, CornerTileStackRectangle> pCorners) {
		this.mBoard = pBoard;
		this.mCorners = pCorners;
		this.mPosition = pPosition;
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
		return mTurn;
	}
	public void setTurn(Position pTurn) {
		this.mTurn = pTurn;
	}
	public int getCenterCount() {
		return this.mCenterCount;
	}
	public void setCenterCount(int pCenterCount) {
		this.mCenterCount = pCenterCount;
	}
	public CornerTileStackRectangle getCornerStack(final TableCorner pTableCorner){
		return mCorners.get(pTableCorner);
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
	public void initializeGame(final Position pTurn, final int pCenterCount, final List<Tile> pUserHand, final Tile pIndicator) {
		
	}
	public synchronized boolean setPendingOperation(final IPendingOperation pIPendingOperation){
		if(this.mIPendingOperation != null){
			return false;
		}
		this.mIPendingOperation = pIPendingOperation;
		return true;
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
		if(this.mTurn == this.mPosition){
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
