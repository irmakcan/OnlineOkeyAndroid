package com.irmakcan.android.okey.gui;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.graphics.Point;
import android.util.Log;

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
	private Text mUserNameText;
	private Text mRemainingTimeText;
	private TimerHandler mTimerHandler;
	// ===========================================================
	// Constructors
	// ===========================================================
	public UserInfoArea(float pX, float pY, float pWidth, float pHeight, 
			VertexBufferObjectManager pVertexBufferObjectManager, Font pUserNameFont, Font pRemainingTimeFont, Position pPosition) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

		mUserNameText = new Text(0, 0, pUserNameFont, "", MAXIMUM_CHARACTERS, new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		mUserNameText.setColor(TEXT_COLOR);
		mUserNameText.setPosition((this.getWidth()/2)-(mUserNameText.getWidth()/2), (this.getHeight()/2)-(mUserNameText.getHeight()/2));
		this.attachChild(mUserNameText);

		mRemainingTimeText = new Text(0, 0, pRemainingTimeFont, "  ", 2, pVertexBufferObjectManager);
		mRemainingTimeText.setColor(TEXT_COLOR);
		Point p = pPosition.getTimerPoint();
		mRemainingTimeText.setPosition(p.x - mRemainingTimeText.getWidth()/2 , p.y - mRemainingTimeText.getHeight()/2);
		this.attachChild(mRemainingTimeText);

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

	public void startTimer(final int pStartTime){
		this.mRemainingTimeText.setText(String.valueOf(pStartTime));
		this.mRemainingTimeText.setVisible(true);
		this.mTimerHandler = new TimerHandler(1, true, new ITimerCallback() {
			private float mRemainingTime = pStartTime;
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mRemainingTime = mRemainingTime - pTimerHandler.getTimerSeconds();
				Log.v("UserInfoArea", "Remaining Time = " + mRemainingTime);
				mRemainingTimeText.setText(String.valueOf((int)mRemainingTime));
				if(mRemainingTime <= 0){
					UserInfoArea.this.unregisterUpdateHandler(mTimerHandler);
				}
			}
		});
		this.registerUpdateHandler(this.mTimerHandler);
	}
	
	public void stopTimer(){
		this.unregisterUpdateHandler(this.mTimerHandler);
		this.mRemainingTimeText.setVisible(false);
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
	public static enum Position{
		// ===========================================================
		// Elements
		// ===========================================================
		TOP(new Point(Constants.USER_AREA_WIDTH/2, Constants.USER_AREA_HEIGHT+Constants.TIMER_PADDING_Y)), 
		BOTTOM(new Point(Constants.USER_AREA_WIDTH/2, -Constants.TIMER_PADDING_Y)), 
		LEFT(new Point(Constants.USER_AREA_WIDTH+Constants.TIMER_PADDING_X, Constants.USER_AREA_HEIGHT/2)), 
		RIGHT(new Point(-Constants.TIMER_PADDING_X, Constants.USER_AREA_HEIGHT/2));
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		private final Point mTimerPoint;
		// ===========================================================
		// Constructors
		// ===========================================================
		private Position(final Point pTimerPoint) {
			this.mTimerPoint = pTimerPoint;
		}
		// ===========================================================
		// Getter & Setter
		// ===========================================================
		public Point getTimerPoint(){
			return this.mTimerPoint;
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
}
