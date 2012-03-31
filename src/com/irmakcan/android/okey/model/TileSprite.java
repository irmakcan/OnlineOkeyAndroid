package com.irmakcan.android.okey.model;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

public class TileSprite extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MAXIMUM_CHARACTERS = 2; 
	// ===========================================================
	// Fields
	// ===========================================================
	
	private final Tile mTile;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public TileSprite(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, 
				Tile pTile, Font pFont) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mTile = pTile;
		
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
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_MOVE:
			this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2); break;
		case TouchEvent.ACTION_DOWN:
			break;
		case TouchEvent.ACTION_UP:
			break;
		default: // Set its position where it is picked up
			break;
		}
		return true;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
