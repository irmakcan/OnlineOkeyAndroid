package com.irmakcan.android.okey.http.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.irmakcan.android.okey.activity.LoginClientActivity;
import com.irmakcan.android.okey.http.SSLClientProvider;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.User;

public class LoginAsyncTask extends AsyncTask<Void, Void, JSONObject>{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final LoginClientActivity mActivity;
	private final String mUrl;
	private final List<NameValuePair> mParams;
	private ProgressDialog mProgressDialog;
	// ===========================================================
	// Constructors
	// ===========================================================

	public LoginAsyncTask(final LoginClientActivity pActivity, final String pUrl, final List<NameValuePair> pParams) {
		this.mActivity = pActivity;
		this.mUrl = pUrl;
		this.mParams = pParams;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		showProgressDialog();
	}

	@Override
	protected JSONObject doInBackground(Void... params) {
		JSONObject json = null;
		try {
			String response = SSLClientProvider.doPost(this.mUrl, this.mParams);
			json = new JSONObject(response);
		} catch (Exception e) {
			json = null;
		}
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject json) {
		super.onPostExecute(json);
		boolean isSuccessful = false;
		List<String> errorList = new ArrayList<String>();
		String message = null;
		String accessToken = null;
		if(json != null){
			try {
				String status = json.getString("status");
				message = json.getString("message");
				if(status.equals("success")){
					accessToken = json.getString("access_token");
					if(accessToken != null && !accessToken.equals("")){
						isSuccessful = true;
					}
				}
			} catch (JSONException e) {
				isSuccessful = false;
			}
		}
		
		hideProgressDialog();
		if(message != null && !message.equals("")){
			Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
		}
		if(!isSuccessful){
			if(message == null || message.equals("")){
				message = "Unknown error occurred. Please try again";
			}
			errorList.add(message);
			mActivity.flash(errorList);
		}else{
			Log.v("message", accessToken);
			Player.setPlayer(new User(mParams.get(0).getValue()));
			mActivity.loginSuccess(accessToken);
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void showProgressDialog(){
		this.mProgressDialog = ProgressDialog.show(mActivity, "Logging In", "Please Wait");
	}
	private void hideProgressDialog(){
		this.mProgressDialog.dismiss();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
