package com.irmakcan.android.okey.model;

import java.util.List;
import java.util.Map;

import com.irmakcan.android.okey.gui.Board;
import com.irmakcan.android.okey.gui.CornerTileStackRectangle;
import com.irmakcan.android.okey.gui.IPendingOperation;


public class TableManager {
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
	public int getCenterCount() {
		return this.mCenterCount;
	}
	public void setmCenterCount(int pCenterCount) {
		this.mCenterCount = pCenterCount;
	}
	public CornerTileStackRectangle getCornerStack(final TableCorner pTableCorner){
		return mCorners.get(pTableCorner);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	private void initializeGame(final Position pTurn, final int pCenterCount, final List<Tile> pUserHand, final Tile pIndicator) {
		
	}
	public synchronized boolean setPendingOperation(final IPendingOperation pIPendingOperation){
		if(this.mIPendingOperation != null){
			return false;
		}
		this.mIPendingOperation = pIPendingOperation;
		return true;
	}
	public void cancelPendingOperation() {
		if(this.mIPendingOperation != null){
			this.mIPendingOperation.cancelPendingOperation();
			this.mIPendingOperation = null;
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	
}
