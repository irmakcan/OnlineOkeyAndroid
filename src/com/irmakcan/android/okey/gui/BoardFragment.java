package com.irmakcan.android.okey.gui;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BoardFragment extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private TileSprite mTileSprite;

	// ===========================================================
	// Constructors
	// ===========================================================
	public BoardFragment(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.setSize(Constants.FRAGMENT_WIDTH, Constants.FRAGMENT_HEIGHT);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	public void addTileSprite(final TileSprite pTileSprite) throws IllegalStateException {
		if(this.mTileSprite != null){
			throw new IllegalStateException("There is already a tile");
		}
		this.mTileSprite = pTileSprite;
		pTileSprite.setPosition(this.getParent().getX() + this.getX(), 
				this.getParent().getY() + this.getY() + (this.getHeight() - pTileSprite.getHeight())); // Set position
	}
	public TileSprite getTileSprite() {
		return this.mTileSprite;
	}
	public TileSprite removeTileSprite() throws IllegalStateException {
		if(this.mTileSprite == null){
			throw new IllegalStateException("No tile sprite has found");
		}
		final TileSprite ts = this.mTileSprite; 
		this.mTileSprite = null;
		return ts;
	}
	public boolean hasTileSprite(){
		return (this.mTileSprite != null);
	}
	
	
	@Override
	public void attachChild(IEntity pEntity) throws IllegalStateException {
		super.attachChild(pEntity);
		if(pEntity instanceof TileSprite){
			TileSprite ts = (TileSprite)pEntity;
			pEntity.setPosition(0, this.getHeight() - ts.getHeight());
			return;
		}
		throw new UnsupportedOperationException("Attaching " + pEntity + " is not supported");
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	

}
