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
	
	private Map<TableCorner, List<TileSprite>> mCorners = new HashMap<TableCorner, List<TileSprite>>();
	private TileSprite mIndicator;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public TableManager() {
		for(TableCorner corner : TableCorner.values()){
			mCorners.put(corner, new ArrayList<TileSprite>());
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
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
