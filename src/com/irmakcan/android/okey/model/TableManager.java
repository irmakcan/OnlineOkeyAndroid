package com.irmakcan.android.okey.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableManager {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private Map<TableCorner, List<Tile>> mCorners = new HashMap<TableCorner, List<Tile>>();
	private Tile mIndicator;
	private int mCenterCount;
//	private Board mBoard;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public TableManager(final User pPlayer/*, final TableUI pTableUI*/) {
		for(TableCorner corner : TableCorner.values()){
			mCorners.put(corner, new ArrayList<Tile>());
		}
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
	private void initializeGame(final Position pTurn, final int pCenterCount, final List<Tile> pUserHand, final Tile pIndicator) {
		
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	
}