package com.irmakcan.android.okey.gui;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Board extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int FRAGMENT_PER_LANE = 12;
	// ===========================================================
	// Fields
	// ===========================================================
	private final BoardFragment[] mLane1 = new BoardFragment[FRAGMENT_PER_LANE];
	private final BoardFragment[] mLane2 = new BoardFragment[FRAGMENT_PER_LANE];
	
	private final float mWidth;
	private final float mHeight;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Board(float pX, float pY, ITextureRegion pBoardFragmentTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, FRAGMENT_PER_LANE*Constants.FRAGMENT_WIDTH, 2*Constants.FRAGMENT_HEIGHT ,pVertexBufferObjectManager);
		final float posY1 = 0;
		final float posY2 = posY1 + Constants.FRAGMENT_HEIGHT;
		float posX = 0;
		for(int i=0;i < FRAGMENT_PER_LANE;i++){
			// Lane 1
			final BoardFragment bf1 = new BoardFragment(posX, posY1, pBoardFragmentTextureRegion, pVertexBufferObjectManager);
			this.attachChild(bf1);
			mLane1[i] = bf1;
			// Lane 2
			final BoardFragment bf2 = new BoardFragment(posX, posY2, pBoardFragmentTextureRegion, pVertexBufferObjectManager);
			this.attachChild(bf2);
			mLane2[i] = bf2;
			
			posX += bf2.getWidth();
		}
		this.mWidth = posX;
		this.mHeight = posY2 + Constants.FRAGMENT_HEIGHT;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public float getWidth() {
		return this.mWidth;
	}
	@Override
	public float getHeight() {
		return this.mHeight;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	public void addChild(TileSprite pTileSprite) throws IllegalStateException {
		// Find first empty board fragment
		BoardFragment bf = null;
		for(int i=0;i < FRAGMENT_PER_LANE;i++){
			if(!mLane1[i].hasTileSprite()){
				bf = mLane1[i];
				break;
			}
		}
		if(bf == null){
			for(int i=0;i < FRAGMENT_PER_LANE;i++){
				if(!mLane2[i].hasTileSprite()){
					bf = mLane2[i];
					break;
				}
			}
		}
		if(bf == null){
			throw new IllegalStateException("Board is full. (Shouldn't be)"); 
		}
		bf.addTileSprite(pTileSprite);
		//pTileSprite.setPosition(this.getX() + bf.getX(), this.getY() + bf.getY());
	}
	public boolean addChild(final TileSprite pTileSprite, int pLocation) { // TODO shift tiles
		if(pLocation < 0 || pLocation > 2*FRAGMENT_PER_LANE){
			throw new IllegalArgumentException("Location " + pLocation + " is out of bound");
		}
		BoardFragment bf = (pLocation < FRAGMENT_PER_LANE ? mLane1[pLocation] : mLane2[pLocation % FRAGMENT_PER_LANE]);
		if(bf.hasTileSprite()){
			return false;
		}
		bf.addTileSprite(pTileSprite);
		return true;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
