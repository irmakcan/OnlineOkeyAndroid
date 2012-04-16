package com.irmakcan.android.okey.gui;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import android.util.Log;

import com.irmakcan.android.okey.model.TableManager;
import com.irmakcan.android.okey.model.Tile;

public class TileSprite extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MAXIMUM_CHARACTERS = 2; 
	// ===========================================================
	// Fields
	// ===========================================================
	private final Tile mTile;
	private final TableManager mTableManager;
	
	private IHolder mIHolder;

	private boolean mTouchEnabled = false;

	private float mOldX;
	private float mOldY;
	// ===========================================================
	// Constructors
	// ===========================================================
	public TileSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, 
			Tile pTile, Font pFont, TableManager pTableManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mTile = pTile;
		this.mTableManager = pTableManager;

		final Text centerText = new Text(0, 0, pFont, Integer.toString(pTile.getValue()), MAXIMUM_CHARACTERS, new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		centerText.setColor(pTile.getTileColor().getColor());
		centerText.setPosition((pTextureRegion.getWidth()/2)-(centerText.getWidth()/2), 4); // TODO
		this.attachChild(centerText);

	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Tile getTile(){
		return this.mTile;
	}
	
	public IHolder getIHolder() {
		return this.mIHolder;
	}
	public void setIHolder(final IHolder pIHolder) {
		this.mIHolder = pIHolder;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(this.mTouchEnabled){
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_MOVE:
//				Log.v("TileSprite", "x: " + pSceneTouchEvent.getX() + " y: " + pSceneTouchEvent.getY());
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight()/2); break;
			case TouchEvent.ACTION_DOWN:
				this.mOldX = this.getX();
				this.mOldY = this.getY();
				this.setZIndex(1);
				this.getParent().sortChildren();
				break;
			case TouchEvent.ACTION_UP:
				if(this.collidesWith(mTableManager.getBoard())){
					Log.v("TileSprite", "Collides: centerX: " + TileSprite.this.getX()+pTouchAreaLocalX + " centerY: " + TileSprite.this.getY()+pTouchAreaLocalY);
					final Board board = mTableManager.getBoard();
					final float centerX = pSceneTouchEvent.getX() - board.getX();
					final float centerY = pSceneTouchEvent.getY() - board.getY();
					if(board.isEmpty(centerX, centerY)){
						if(mTableManager.getPreviousCornerStack().lastElement() == this){
							IPendingOperation drawPendingOperation = new IPendingOperation() {
								@Override
								public void pendingOperationSuccess(Object o) {
									CornerTileStackRectangle tileStack = (CornerTileStackRectangle)o;
									tileStack.pop();
									mTableManager.moveTile(mMovePendingOperation, TileSprite.this, centerX, centerY);
								}
								@Override
								public void cancelPendingOperation() {
									TileSprite.this.setPosition(mOldX, mOldY);
								}
							};
							mTableManager.drawCornerTile(drawPendingOperation);
						}else{ // normal move
							mTableManager.moveTile(mMovePendingOperation, this, centerX, centerY);
						}
					}else{
						this.setPosition(mOldX, mOldY);
					}
					
					
//					boolean success = board.addChild(this, (pSceneTouchEvent.getX() - board.getX()), (pSceneTouchEvent.getY() - board.getY()));
//					if(!success){
////						cancelPendingOperation();
//					}else{
//						// TODO remove from board fragment
//					}
				}else if(this.collidesWith(mTableManager.getNextCornerStack())){
					Log.v("TileSprite", "Collides: centerX: " + TileSprite.this.getX()+pTouchAreaLocalX + "centerY: " + TileSprite.this.getY()+pTouchAreaLocalY);
					this.mTableManager.throwTile(mThrowPendingOperation, this.getTile());
//				}else if(this.collidesWith(mTableManager.getCenterStack())){ // Throw to finish

				}else{
					this.setPosition(mOldX, mOldY);// Send it back where it comes from
				}
				this.setZIndex(0);
				this.getParent().sortChildren();
				break;
			default: // Set its position where it is picked up
				break;
			}
		}
		return true;
	}

//	@Override
//	public void cancelPendingOperation() {
//		this.setPosition(mOldX, mOldY);
//	}
//	@Override
//	public void pendingOperationSuccess(Object o) {
//		// TODO
//	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void enableTouch() {
		this.mTouchEnabled = true;
	}
	public void disableTouch() {
		this.mTouchEnabled = false;
	}
	public IPendingOperation getMovePendingOperation() {
		return mMovePendingOperation;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private IPendingOperation mMovePendingOperation = new IPendingOperation() {
		@Override
		public void pendingOperationSuccess(Object o) {
			BoardFragment boardFragment = (BoardFragment)o;
			if(getIHolder() != null){
				mIHolder.removeTileSprite();
			}
			setIHolder(boardFragment);
		}
		@Override
		public void cancelPendingOperation() {
			TileSprite.this.setPosition(mOldX, mOldY);
		}
	};
	private IPendingOperation mThrowPendingOperation = new IPendingOperation() {
		@Override
		public void pendingOperationSuccess(Object o) {
			mIHolder.removeTileSprite();
			CornerTileStackRectangle tileStack = (CornerTileStackRectangle)o;
			TileSprite.this.disableTouch();
			tileStack.push(TileSprite.this);
		}
		@Override
		public void cancelPendingOperation() {
			TileSprite.this.setPosition(mOldX, mOldY);
		}
	};
}
