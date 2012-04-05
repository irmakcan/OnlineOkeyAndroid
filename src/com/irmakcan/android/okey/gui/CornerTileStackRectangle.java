package com.irmakcan.android.okey.gui;

import java.util.Stack;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.irmakcan.android.okey.model.TableCorner;

public class CornerTileStackRectangle extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private final Stack<TileSprite> mTileStack = new Stack<TileSprite>();
	private final TableCorner mTableCorner;

	// ===========================================================
	// Constructors
	// ===========================================================
	public CornerTileStackRectangle(float pX, float pY, float pWidth, float pHeight, 
			VertexBufferObjectManager pVertexBufferObjectManager, final TableCorner pTableCorner) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.setZIndex(0);
		this.mTableCorner = pTableCorner;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public TableCorner getTableCorner() {
		return this.mTableCorner;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public TileSprite pop() {
		final TileSprite tile = mTileStack.pop();
		return tile;
	}
	public void push(final TileSprite pTileSprite) {
		pTileSprite.setZIndex(this.mTileStack.size());
		pTileSprite.getParent().sortChildren();
		pTileSprite.setPosition(this);
		
		mTileStack.push(pTileSprite);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
