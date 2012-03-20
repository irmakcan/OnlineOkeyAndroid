package com.irmakcan.android.okey.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.widget.TextView;

import com.irmakcan.android.okey.R;
import com.irmakcan.android.okey.http.SSLClientProvider;

public class HttpsTestActivity extends Activity{

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView tv = (TextView) findViewById(R.id.text_view);

		//DefaultHttpClient httpClient = SSLClientProvider.getSSLHttpClient();

		// Set params
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user[username]", "irmak"));
		nameValuePairs.add(new BasicNameValuePair("user[password]", "mercedes"));
		
		// Example send http request
		final String url = "https://evening-stream-2301.herokuapp.com/okey/login";
		//final String url = "https://encrypted.google.com/";
		
		try {
			String s = SSLClientProvider.doPost(url, nameValuePairs);
			tv.setText(s);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
