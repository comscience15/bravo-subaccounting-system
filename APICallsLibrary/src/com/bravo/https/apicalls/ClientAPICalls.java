package com.bravo.https.apicalls;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.https.util.BravoHttpsClient;
import com.bravo.https.util.HttpResponseHandler;

import android.content.Context;

public class ClientAPICalls {
	private static Logger logger;
	static {
		logger = Logger.getLogger(ClientAPICalls.class.getName());
	}
	/**
	 * This method is to get the cardList from server
	 * The request should bundle with latest got cookie in order to pass the authentication
	 * @param IP
	 * @param androidContext
	 * @return
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static ArrayList<JSONObject> getCardListByCustID(String IP, Context androidContext) 
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, JSONException {
		final String ip = IP;
		final String path = "service/customer/account/getCardListByCustID";
		final String URL = ip + path;
		
		FileInputStream getStoredCookie = null;
		byte[] cookieByte = null;
		int cookieSize = 0;
		
		try {
			getStoredCookie = androidContext.openFileInput("Cookie");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			cookieSize = getStoredCookie.available();
			cookieByte = new byte[cookieSize];
			getStoredCookie.read(cookieByte);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String cookie = new String(cookieByte);
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, null, cookie, "getCardList", androidContext);
		
		return HttpResponseHandler.toArrayList(response);	
	}
}
