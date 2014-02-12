package com.bravo.https.apicalls;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bravo.https.util.BravoAuthenticationException;
import com.bravo.https.util.BravoHttpsClient;
import com.bravo.https.util.CookieHandler;
import com.bravo.https.util.HttpResponseHandler;

public class MerchantAPICalls {
	
	public static String purchaseItems(String IP, Context androidContext, List<NameValuePair> params) throws IOException, KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, JSONException, BravoAuthenticationException {
		final String ip = IP;
		final String path = "/service/merchant/transaction/purchaseItems";
		final String URL = ip + path;
		
		String cookie = CookieHandler.getCookie(androidContext);
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, params, cookie, "purchaseItems", androidContext);
		String msg = HttpResponseHandler.toString(response);
		
		return msg;
	}

}
