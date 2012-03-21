package com.irmakcan.android.okey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.irmakcan.android.okey.R;

public class MainMenuActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		Button login = (Button)findViewById(R.id.mainscreen_login_button);
		Button signup = (Button)findViewById(R.id.mainscreen_signup_button);
		login.setOnClickListener(mLoginAction);
		signup.setOnClickListener(mSignUpAction);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	OnClickListener mLoginAction = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainMenuActivity.this, LoginClientActivity.class);
			startActivity(i);
		}
	};
	OnClickListener mSignUpAction = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainMenuActivity.this, SignUpClientActivity.class);
			startActivity(i);
		}
	};
}
