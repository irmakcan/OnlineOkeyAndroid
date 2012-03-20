package com.irmakcan.android.okey.http;

import java.io.IOException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class SSLClientProvider {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private static DefaultHttpClient mHttpClient;
	// ===========================================================
	// Constructors
	// ===========================================================
	private SSLClientProvider() {
		// TODO Auto-generated constructor stub
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

	private static DefaultHttpClient getSSLHttpClient(){
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		DefaultHttpClient client = new DefaultHttpClient();

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

		// Set verifier      
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		return httpClient;
	}

	public static String doPost(final String pUrl, final List<NameValuePair> pParams) throws ClientProtocolException, IOException{
		mHttpClient = getSSLHttpClient();

		HttpPost httpPost = new HttpPost(pUrl);

		String response;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pParams));

			HttpResponse httpResponse = mHttpClient.execute(httpPost);
			//
			// Examine the response status
			Log.v("message", httpResponse.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = httpResponse.getEntity();

			response = EntityUtils.toString(entity);
			Log.v("message", response);
		} finally {
			mHttpClient.getConnectionManager().shutdown();
		}

		return response;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
