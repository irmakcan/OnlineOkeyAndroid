package com.irmakcan.android.okey.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
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
	private CheckBox mCheckBox;

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
		this.mCheckBox = (CheckBox)findViewById(R.id.loginscreen_checkbox_rememberme);
		Button loginButton = (Button)findViewById(R.id.loginscreen_button_login);
		loginButton.setOnClickListener(mLoginAction);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadUserSettings();
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

	private void loadUserSettings(){
		SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
		String username = sharedPreferences.getString("username", "");
		String password = sharedPreferences.getString("password", "");
		boolean checkBoxState = sharedPreferences.getBoolean("checkbox", false);
		this.mUsernameText.setText(username);
		this.mPasswordText.setText(password);
		this.mCheckBox.setChecked(checkBoxState);
	}

	private void saveUserSettings(){
		Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();
		e.putString("username", this.mUsernameText.getText().toString());
		e.putString("password", this.mPasswordText.getText().toString());
		e.putBoolean("checkbox", this.mCheckBox.isChecked());
		e.commit();
	}
	
	private void deleteUserSettings(){
		Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();
		e.remove("username");
		e.remove("password");
		e.remove("checkbox");
		e.commit();
	}
	
	public void loginSuccess(String pAccessToken) {
		// Save user infos if Remember me is checked
		if(this.mCheckBox.isChecked()){
			saveUserSettings();
		}else{
			deleteUserSettings();
		}
		// Launch new activity TODO
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
				final String url = "https://okey.herokuapp.com/okey/login";
				LoginAsyncTask task = new LoginAsyncTask(LoginClientActivity.this, url, nameValuePairs); // TODO
				task.execute();
			}
		}
	};

}
