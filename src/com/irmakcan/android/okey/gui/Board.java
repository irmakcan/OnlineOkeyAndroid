package com.irmakcan.android.okey.gui;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.irmakcan.android.okey.model.Tile;

public class Board extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int FRAGMENT_PER_LANE = 13;
	private static final String LOG_TAG = "Board";
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
	public Board(float pX, float pY, ITextureRegion pBoardFragmentTextureRegion, ITextureRegion pBoardSideTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, FRAGMENT_PER_LANE*Constants.FRAGMENT_WIDTH, 2*Constants.FRAGMENT_HEIGHT, pVertexBufferObjectManager);
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
		
		pBoardSideTextureRegion.setTextureWidth(22f);
		// Sides
		this.attachChild(new Sprite(-pBoardSideTextureRegion.getWidth(), 0, pBoardSideTextureRegion.getWidth(), this.mHeight, pBoardSideTextureRegion, pVertexBufferObjectManager));
		this.attachChild(new Sprite(this.mWidth, 0, pBoardSideTextureRegion.getWidth(), this.mHeight, pBoardSideTextureRegion, pVertexBufferObjectManager));
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
	public List<List<Tile>> getHandWithoutTile(final Tile pTile){
		List<List<Tile>> hand = new ArrayList<List<Tile>>();
		List<Tile> group = null;
		for(int i=0;i<mLane1.length;i++){
			if(!mLane1[i].hasTileSprite()){
				if(group != null){
					hand.add(group);
					group = null;
				}
			}else{
				Tile tile = mLane1[i].getTileSprite().getTile();
				if(tile != pTile){ 
					if(group == null){
						group = new ArrayList<Tile>();
					}
					group.add(tile);
				}
			}
		}
		if(group != null){
			hand.add(group);
			group = null;
		}
		for(int i=0;i<mLane2.length;i++){
			if(!mLane2[i].hasTileSprite()){
				if(group != null){
					hand.add(group);
					group = null;
				}
			}else{
				Tile tile = mLane2[i].getTileSprite().getTile();
				if(tile != pTile){ 
					if(group == null){
						group = new ArrayList<Tile>();
					}
					group.add(tile);
				}
			}
		}
		if(group != null){
			hand.add(group);
			group = null;
		}
		for(List<Tile> g : hand){ //TODO
			for(Tile t : g){
				Log.v(LOG_TAG, t.toString());
			}
		}
		return hand;
	}
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
		pTileSprite.setIHolder(bf);
		//pTileSprite.setPosition(this.getX() + bf.getX(), this.getY() + bf.getY());
	}
	
	public synchronized BoardFragment addChild(final TileSprite pTileSprite, final float pX, final float pY){
		Log.v(LOG_TAG, "pX: " + pX + " pY: " + pY);
		BoardFragment[] lane = getLane(pY);
		int column = getColumn(pX);
		if(lane[column].hasTileSprite()){
			return null;
		}
		lane[column].addTileSprite(pTileSprite);
		return lane[column];
	}
	
	public boolean addChild(TileSprite pTileSprite, int pLocation){
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
	
	public boolean isEmpty(final float pX, final float pY){
		Log.v(LOG_TAG, "pX: " + pX + " pY: " + pY);
		BoardFragment[] lane = getLane(pY);
		int column = getColumn(pX);
		if(lane[column].hasTileSprite()){
			return false;
		}
		return true;
	}
	
	public TileSprite getFirstTileSprite(final Tile pTile){
		for(int i=0;i < FRAGMENT_PER_LANE;i++){
			if(mLane1[i].hasTileSprite()){
				TileSprite ts = mLane1[i].getTileSprite();
				if(ts.getTile().equals(pTile)){
					return ts;
				}
			}
		}
		for(int i=0;i < FRAGMENT_PER_LANE;i++){
			if(mLane2[i].hasTileSprite()){
				TileSprite ts = mLane2[i].getTileSprite();
				if(ts.getTile().equals(pTile)){
					return ts;
				}
			}
		}
		throw new IllegalStateException("Tile " + pTile.toString() + " cannot be found on the board."); 
	}
	
	private int getColumn(final float pX){
		int column = (int)(pX / Constants.FRAGMENT_WIDTH);
		if(column < 0){
			column = 0; 
		}else if(column >= FRAGMENT_PER_LANE){
			column = FRAGMENT_PER_LANE - 1;
		}
		Log.v(LOG_TAG, "Column: " + column);
		return column;
	}
	private BoardFragment[] getLane(final float pY){
		BoardFragment[] lane;
		if(pY < Constants.FRAGMENT_HEIGHT){
			Log.v(LOG_TAG, "1st lane");
			lane = this.mLane1;
		}else{
			Log.v(LOG_TAG, "2nd lane");
			lane = this.mLane2;
		}
		return lane;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
