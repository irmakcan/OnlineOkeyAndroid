package com.irmakcan.android.okey.gui;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

public class ChatWindow extends Rectangle{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int MAXIMUM_CHARACTERS_PER_LINE = 54;
	private static final float TEXT_PADDING_X = 4;
	private static final float TEXT_PADDING_Y = 4;
	// ===========================================================
	// Fields
	// ===========================================================
	private List<Text> mChatMessages;
	private Font mFont;
	private VertexBufferObjectManager mVertexBufferObjectManager;
	// ===========================================================
	// Constructors
	// ===========================================================
	public ChatWindow(float pX, float pY, float pWidth, float pHeight, Font pFont, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.mFont = pFont;
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
		this.mChatMessages = new ArrayList<Text>();
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
	public void addMessage(final String pDate, final String pMessage){
		String message = pDate + " " + pMessage;
		while(message.length() > MAXIMUM_CHARACTERS_PER_LINE){
			String msg = message.substring(0, MAXIMUM_CHARACTERS_PER_LINE);
			this.addMessage(msg);
			message = message.substring(MAXIMUM_CHARACTERS_PER_LINE);
		}
		this.addMessage(message);
	}
	private void addMessage(final String pMessage){
		final Text messageText = new Text(0, 0, mFont, pMessage, MAXIMUM_CHARACTERS_PER_LINE, new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
		messageText.setAlpha(0.8f);
		for(Text text : this.mChatMessages){
			text.setPosition(text.getX(), text.getY() - messageText.getHeight());
		}
		messageText.setPosition(TEXT_PADDING_X, this.getHeight() - (messageText.getHeight() + TEXT_PADDING_Y));
		this.mChatMessages.add(messageText);
		this.attachChild(messageText);
	}
	public boolean isOpen(){
		return this.isVisible();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
