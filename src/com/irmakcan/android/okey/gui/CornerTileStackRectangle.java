package com.irmakcan.android.okey.gui;

import java.util.Stack;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
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
	private final Scene mScene;

	// ===========================================================
	// Constructors
	// ===========================================================
	public CornerTileStackRectangle(float pX, float pY, float pWidth, float pHeight, 
			VertexBufferObjectManager pVertexBufferObjectManager, final TableCorner pTableCorner, final Scene pScene) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.setAlpha(0.7f);
		this.setZIndex(0);
		this.mTableCorner = pTableCorner;
		this.mScene = pScene;
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
		final TileSprite lastElement = this.lastElement();
		if(lastElement != null){
			this.mScene.unregisterTouchArea(lastElement);
			lastElement.disableTouch();
		}
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
