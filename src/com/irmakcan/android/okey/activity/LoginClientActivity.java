package com.irmakcan.android.okey.activity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.irmakcan.android.okey.R;
import com.irmakcan.android.okey.gson.BaseResponse;
import com.irmakcan.android.okey.http.task.LoginAsyncTask;
import com.irmakcan.android.okey.websocket.WebSocketProvider;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

public class LoginClientActivity extends Activity {
	
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "LoginClientActivity";
//	private static final String HOST_URL = "192.168.1.100:8080";
	private static final String HOST_URL = "www.okey.irmakcan.com:8080";
	// ===========================================================
	// Fields
	// ===========================================================
	private EditText mUsernameText;
	private EditText mPasswordText;
	private ScrollView mScrollView;
	private LinearLayout mFlash;
	private CheckBox mCheckBox;
	private String mCurrentUserName;
	private ProgressDialog mProgressDialog;

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
	private void showProgressDialog(){
		this.mProgressDialog = ProgressDialog.show(this, "Connecting to the Okey Server", "Please Wait");
	}
	private void hideProgressDialog(){
		this.mProgressDialog.dismiss();
	}
	public void loginSuccess(final String pAccessToken) {
		// Save user infos if Remember me is checked
		if(this.mCheckBox.isChecked()){
			saveUserSettings();
		}else{
			deleteUserSettings();
		}
		
		// connect to the websocket server
		
		try {
			showProgressDialog(); // TODO not showing up check
			URI url = new URI("ws://" + HOST_URL + "/");
			WebSocket ws = WebSocketProvider.createWebSocketConnection(url);
			ws.setEventHandler(mWebSocketEventHandler);
			ws.connect();
			JSONObject json = new JSONObject();
			json.put("action", "authenticate").put("version", "0.0.0")
					.put("username", this.mCurrentUserName).put("access_token", pAccessToken);
			ws.send(json.toString()); // TODO authentication
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.v(LOG_TAG, "Hide progress dialog");
			hideProgressDialog();
		}
		
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
				LoginClientActivity.this.mCurrentUserName = username;
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
	
	private WebSocketEventHandler mWebSocketEventHandler = new WebSocketEventHandler() {
		@Override
		public void onOpen() {
			Log.v(LOG_TAG, "WebSocket connected");
		}
		@Override
		public void onMessage(WebSocketMessage message) {
			Log.v(LOG_TAG, "Message received: " + message.getText());
			Gson gson = new Gson();
			BaseResponse baseResponse = gson.fromJson(message.getText(), BaseResponse.class);
			if(baseResponse.getStatus().equals("success")){
				Log.v(LOG_TAG, "auth success");
				Intent i = new Intent(LoginClientActivity.this, OkeyLoungeActivity.class);
				startActivity(i);
			}else{
				// TODO
			}
		}
		@Override
		public void onClose() {
			Log.v(LOG_TAG, "WebSocket disconnected");
		}
	};

}
