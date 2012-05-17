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
	private int mOldZIndex;
	// ===========================================================
	// Constructors
	// ===========================================================
	public TileSprite(float pX, float pY, ITextureRegion pTextureRegion, ITextureRegion pFakeJokerTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, 
			Tile pTile, Font pFont, TableManager pTableManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mTile = pTile;
		this.mTableManager = pTableManager;

		this.mOldX = pX;
		this.mOldY = pY;
		
		if(pTile.getValue() == 0){
			Sprite jokerSprite = new Sprite(0, 0, pFakeJokerTextureRegion, pVertexBufferObjectManager);
			jokerSprite.setScale(0.81f);
			jokerSprite.setPosition((pTextureRegion.getWidth()/2)-(jokerSprite.getWidth()/2), 0);
			this.attachChild(jokerSprite);
		}else{
			final Text centerText = new Text(0, 0, pFont, Integer.toString(pTile.getValue()), MAXIMUM_CHARACTERS, new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
			centerText.setColor(pTile.getTileColor().getColor());
			centerText.setPosition((pTextureRegion.getWidth()/2)-(centerText.getWidth()/2), 1);
			if(pTile.getValue() == 1){
				centerText.setX(centerText.getX()-4);
			}else if(pTile.getValue() == 11){
				centerText.setX(centerText.getX()-2);
			}
			this.attachChild(centerText);
		}

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
	public synchronized boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(this.mTouchEnabled){
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_MOVE:
//				Log.v("TileSprite", "x: " + pSceneTouchEvent.getX() + " y: " + pSceneTouchEvent.getY());
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight()/2); break;
			case TouchEvent.ACTION_DOWN:
				this.mOldX = this.getX();
				this.mOldY = this.getY();
				this.mOldZIndex = this.getZIndex();
				this.setZIndex(35);
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
								@Override
								public Tile getPendingTile() {
									return TileSprite.this.getTile();
								}
							};
							mTableManager.drawCornerTile(drawPendingOperation);
						}else{ // normal move
							mTableManager.moveTile(mMovePendingOperation, this, centerX, centerY);
						}
					}else{
						this.setPosition(mOldX, mOldY);
					}
					
				}else if(this.collidesWith(mTableManager.getNextCornerStack())){
					Log.v("TileSprite", "Collides: centerX: " + TileSprite.this.getX()+pTouchAreaLocalX + "centerY: " + TileSprite.this.getY()+pTouchAreaLocalY);
					this.mTableManager.throwTile(mThrowPendingOperation, this.getTile());
				}else if(this.collidesWith(mTableManager.getCenterArea())){ // Throw to finish
					this.mTableManager.throwTileToFinish(mThrowToFinishPendingOperation, this.getTile());
				}else{
					this.setPosition(mOldX, mOldY);// Send it back where it comes from
				}
				this.setZIndex(this.mOldZIndex);
				this.getParent().sortChildren();
				break;
			default: // Set its position where it is picked up
				break;
			}
		}
		return true;
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
	public IPendingOperation getMovePendingOperation() {
		return mMovePendingOperation;
	}
	
	public void setTilePosition(float pX, float pY) {
		this.mOldX = pX;
		this.mOldY = pY;
		this.setPosition(pX, pY);
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
		@Override
		public Tile getPendingTile() {
			return TileSprite.this.getTile();
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
		@Override
		public Tile getPendingTile() {
			return TileSprite.this.getTile();
		}
	};
	private IPendingOperation mThrowToFinishPendingOperation = new IPendingOperation() {
		@Override
		public void pendingOperationSuccess(Object o) {
			mIHolder.removeTileSprite();
			TileSprite.this.disableTouch();
			TileSprite.this.dispose();
			TileSprite.this.detachSelf();
		}
		@Override
		public void cancelPendingOperation() {
			TileSprite.this.setPosition(mOldX, mOldY);
		}
		@Override
		public Tile getPendingTile() {
			return TileSprite.this.getTile();
		}
	};
}
