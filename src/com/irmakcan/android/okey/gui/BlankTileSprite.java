package com.irmakcan.android.okey.gui;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.irmakcan.android.okey.model.TableManager;

public class BlankTileSprite extends Sprite implements IPendingOperation{
	// ===========================================================
	// Constants
	// ===========================================================

	private final float mX;
	private final float mY;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mTouchEnabled;
	private TableManager mTableManager;
	
	private float mCenterX;
	private float mCenterY;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public BlankTileSprite(float pX, float pY, ITextureRegion pTextureRegion, 
			VertexBufferObjectManager pVertexBufferObjectManager, TableManager pTableManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mX = pX;
		mY = pY;
		this.mTableManager = pTableManager;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(this.mTouchEnabled){
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_MOVE:
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2); break;
			case TouchEvent.ACTION_DOWN:
				break;
			case TouchEvent.ACTION_UP:
				if(this.collidesWith(this.mTableManager.getBoard())){
					final Board board = this.mTableManager.getBoard();
					
					mCenterX = pSceneTouchEvent.getX() - board.getX();
					mCenterY = pSceneTouchEvent.getY() - board.getY();
					
					if(mTableManager.getBoard().isEmpty(mCenterX, mCenterY)){
						mTableManager.drawCenterTile(this);
					} else {
						cancelPendingOperation();
					}
				}else{
					// Send it back where it comes from
					cancelPendingOperation();
				}
				break;
			default: // Set its position where it was picked up
				cancelPendingOperation();
				break;
			}
		}
		return true;
	}

	@Override
	public void cancelPendingOperation() {
		this.setPosition(mX, mY);
	}

	@Override
	public void pendingOperationSuccess(Object o) {
		this.setPosition(mX, mY);
		TileSprite ts = (TileSprite)o;
		mTableManager.moveTile(ts.getMovePendingOperation(), ts, mCenterX, mCenterY);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public void enableTouch() {
		this.mTouchEnabled = true;
	}
	public void disableTouch() {
		this.mTouchEnabled = false;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	
}
