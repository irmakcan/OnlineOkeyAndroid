package com.irmakcan.android.okey.gui;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.irmakcan.android.okey.model.TableCorner;

public class CornerRectangle extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	//Stack<E>
	private final TableCorner mTableCorner;

	// ===========================================================
	// Constructors
	// ===========================================================
	public CornerRectangle(float pX, float pY, float pWidth, float pHeight, 
			VertexBufferObjectManager pVertexBufferObjectManager, final TableCorner pTableCorner) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		
		this.mTableCorner = pTableCorner;
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
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
