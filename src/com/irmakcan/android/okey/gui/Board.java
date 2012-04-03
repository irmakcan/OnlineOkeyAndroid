package com.irmakcan.android.okey.gui;

import java.util.ArrayList;
import java.util.List;

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
	private final List<BoardFragment> mLane1 = new ArrayList<BoardFragment>();
	private final List<BoardFragment> mLane2 = new ArrayList<BoardFragment>();
	//private final Rectangle mLan = new Recta;
	
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
			mLane1.add(bf1);
			// Lane 2
			final BoardFragment bf2 = new BoardFragment(posX, posY2, pBoardFragmentTextureRegion, pVertexBufferObjectManager);
			this.attachChild(bf2);
			mLane2.add(bf2);
			
			posX += bf2.getWidth();
		}
		this.mWidth = posX;
		this.mHeight = posY2 + Constants.FRAGMENT_HEIGHT;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public float getWidth() {
		return this.mWidth;
	}
	public float getHeight() {
		return this.mHeight;
	}
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
