package com.irmakcan.android.okey.gui;

import java.util.Stack;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.irmakcan.android.okey.model.TableCorner;

public class CornerTileStackRectangle extends Rectangle implements IHolder{
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
	@Override
	public void addTileSprite(TileSprite pTileSprite) {
		this.push(pTileSprite);
	}
	@Override
	public TileSprite getTileSprite() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TileSprite removeTileSprite() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasTileSprite() {
		// TODO Auto-generated method stub
		return false;
	}
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
	public TileSprite lastElement(){
		if(this.mTileStack.size() > 0){
			return this.mTileStack.lastElement();
		}
		return null;

	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
