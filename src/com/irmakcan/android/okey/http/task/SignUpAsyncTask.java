package com.irmakcan.android.okey.http.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.irmakcan.android.okey.activity.SignUpClientActivity;
import com.irmakcan.android.okey.http.SSLClientProvider;

public class SignUpAsyncTask extends AsyncTask<Void, Void, JSONObject>{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final SignUpClientActivity mActivity;
	private final String mUrl;
	private final List<NameValuePair> mParams;
	private ProgressDialog mProgressDialog;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SignUpAsyncTask(final SignUpClientActivity pActivity, final String pUrl, final List<NameValuePair> pParams) {
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
		if(json != null){
			try {
				String status = json.getString("status");
				message = json.getString("message");
				if(status.equals("success")){
					isSuccessful = true;
				} else if(status.equals("error")){
					JSONArray reasons = json.getJSONArray("reasons");
					for(int index=0;index<reasons.length();index++){
						errorList.add(reasons.getString(index));
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
			if(errorList.isEmpty()){
				errorList.add("Unknown error occurred. Please try again"); // TODO localization
			}
			mActivity.flash(errorList);
		}else{
			//mActivity.fi TODO
		}
		
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void showProgressDialog(){
		this.mProgressDialog = ProgressDialog.show(mActivity, "Signing Up", "Please Wait!"); // TODO localization
	}
	private void hideProgressDialog(){
		this.mProgressDialog.dismiss();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
