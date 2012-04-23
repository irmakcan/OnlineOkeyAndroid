package com.irmakcan.android.okey.gui;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class TileCountText extends Text {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MAXIMUM_CHARACTER = 2;
	private static final Color TEXT_COLOR = new Color(0f, 0f, 0f);
	private static final float TEXT_ALPHA = 0.7f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final float mReferenceX;
	private final float mReferenceY;
	// ===========================================================
	// Constructors
	// ===========================================================
	public TileCountText(float pReferenceX, float pReferenceY, IFont pFont,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pFont, "", MAXIMUM_CHARACTER, pVertexBufferObjectManager);
		this.mReferenceX = pReferenceX;
		this.mReferenceY = pReferenceY;
		this.setColor(TEXT_COLOR);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setCount(int pCount){
		this.setText(Integer.toString(pCount));
		this.setPosition(mReferenceX + (Constants.TILE_WIDTH-getWidth())/2, mReferenceY + (Constants.TILE_HEIGHT-getHeight())/2);
		this.setAlpha(TEXT_ALPHA);
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
