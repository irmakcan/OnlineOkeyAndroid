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
	private final int MAXIMUM_CHARACTERS = 12;
	private final Color TEXT_COLOR = new Color(0.5f, 0.1f, 0.4f);
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
