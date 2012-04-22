package com.irmakcan.android.okey.gui;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import com.irmakcan.android.okey.model.User;

public class UserInfoArea extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MAXIMUM_CHARACTERS = 12;
	private static final Color TEXT_COLOR = new Color(0.5f, 0.1f, 0.4f);
	private static final Color BACKGROUND_COLOR = new Color(0.863f, 0.925f, 0.824f);
	private static final Color ENABLED_BACKGROUND_COLOR = new Color(0.702f, 0.808f, 0.984f);
	private static final float BACKGROUND_ALPHA = 0.5f;
	private static final float ENABLED_BACKGROUND_ALPHA = 0.9f;
	// ===========================================================
	// Fields
	// ===========================================================
	Text mUserNameText;
	// ===========================================================
	// Constructors
	// ===========================================================
	public UserInfoArea(float pX, float pY, float pWidth, float pHeight, 
			VertexBufferObjectManager pVertexBufferObjectManager, Font pFont) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

		mUserNameText = new Text(0, 0, pFont, "", MAXIMUM_CHARACTERS, new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		mUserNameText.setColor(TEXT_COLOR);
		mUserNameText.setPosition((this.getWidth()/2)-(mUserNameText.getWidth()/2), (this.getHeight()/2)-(mUserNameText.getHeight()/2)); // TODO
		this.attachChild(mUserNameText);
		this.setColor(BACKGROUND_COLOR);
		this.setAlpha(BACKGROUND_ALPHA);
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
	public void setEnabled(final boolean pEnabled){
		if(pEnabled){
			this.setColor(ENABLED_BACKGROUND_COLOR);
			this.setAlpha(ENABLED_BACKGROUND_ALPHA);
		}else{
			this.setColor(BACKGROUND_COLOR);
			this.setAlpha(BACKGROUND_ALPHA);
		}
	}
	
	public void setTextFrom(final User pUser){
		if(pUser == null || pUser.getUserName() == null){
			this.setText("");
		}else{
			this.setText(pUser.getUserName());
		}
	}
	private void setText(String pText) {
		if(pText.length() > MAXIMUM_CHARACTERS){
			pText = pText.substring(0, MAXIMUM_CHARACTERS-2) + "..";
		}
		this.mUserNameText.setText(pText);
		this.mUserNameText.setPosition((this.getWidth()/2)-(mUserNameText.getWidth()/2), (this.getHeight()/2)-(mUserNameText.getHeight()/2));
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	//	static enum ArrowPosition{
	//		TOP, BOTTOM, LEFT, RIGHT;
	//	}
}
