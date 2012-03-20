package com.irmakcan.android.okey.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.irmakcan.android.okey.R;
import com.irmakcan.android.okey.http.task.LoginAsyncTask;

public class LoginClientActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private EditText mUsernameText;
	private EditText mPasswordText;
	private ScrollView mScrollView;
	private LinearLayout mFlash;
	
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
		setContentView(R.layout.login_screen);
		
		this.mUsernameText = (EditText)findViewById(R.id.loginscreen_edittext_username);
		this.mPasswordText = (EditText)findViewById(R.id.loginscreen_edittext_password);
		this.mScrollView = (ScrollView)findViewById(R.id.loginscreen_scroll_view);
		this.mFlash = (LinearLayout)findViewById(R.id.loginscreen_layout_flash);
		Button loginButton = (Button)findViewById(R.id.loginscreen_button_login);
		loginButton.setOnClickListener(mLoginAction);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public void flash(final List<String> pErrorList){
		this.mFlash.removeAllViews();
		for(String errorMessage: pErrorList){
			TextView tv = new TextView(this);
			tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tv.setTextAppearance(this, R.style.ErrorFont);
			tv.setText(errorMessage);
			this.mFlash.addView(tv);
		}
		this.mFlash.setVisibility(View.VISIBLE);
		this.mScrollView.fullScroll(ScrollView.FOCUS_UP);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private OnClickListener mLoginAction = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String username = mUsernameText.getText().toString();
			String password = mPasswordText.getText().toString();
			
			if(username.equals("") || password.equals("")){
				List<String> errorList = new ArrayList<String>();
				errorList.add("Fields can't be blank");
				flash(errorList);
			}else{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user[username]", username));
				nameValuePairs.add(new BasicNameValuePair("user[password]", password));
				
				// Example send http request
				final String url = "https://evening-stream-2301.herokuapp.com/okey/login";
				LoginAsyncTask task = new LoginAsyncTask(LoginClientActivity.this, url, nameValuePairs); // TODO
				task.execute();
			}
		}
	};
}
