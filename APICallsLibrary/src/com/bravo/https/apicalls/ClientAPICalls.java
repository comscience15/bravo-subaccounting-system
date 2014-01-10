package com.bravo.https.apicalls;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.https.util.BravoHttpsClient;
import com.bravo.https.util.HttpResponseHandler;

import android.content.Context;

public class ClientAPICalls {
	private static Logger logger = Logger.getLogger(ClientAPICalls.class.getName());
	
	/**
	 * Get public key API
	 * @param IP
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws UnrecoverableKeyException 
	 * @throws KeyManagementException 
	 */
	public static Hashtable<String, BigInteger> getPublicKey(String IP, Context androidContext) 
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		final String ip = IP;
		final String path = "service/customer/transaction/getKey";
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
		
		logger.log(Level.INFO, "getKey: Cookie is :" + cookie);
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, null, cookie, "getKey", androidContext);
		
		String responseString = HttpResponseHandler.toString(response);
		String getKeyStatus = HttpResponseHandler.parseJson(responseString, "status");
		
		Hashtable<String, BigInteger> publickey = new Hashtable<String, BigInteger>();
		
		// Check if authentication is needed for getting keys
		if (getKeyStatus != null && getKeyStatus.equals("404")) {
			// Should go to login first
			logger.log(Level.SEVERE, "Should go to login first");
			publickey.put("status", BigInteger.valueOf(404));
			return null;
		} else {
			String e = HttpResponseHandler.parseJson(responseString, "e");
			String n = HttpResponseHandler.parseJson(responseString, "n");
			String maxdigits = HttpResponseHandler.parseJson(responseString, "maxdigits");
		
			logger.log(Level.INFO, "e: " + e + " n: " + n + " max:" + maxdigits);
		
			BigInteger eInt = new BigInteger(e, 16);
			BigInteger nInt = new BigInteger(n, 16);
			BigInteger maxInt = new BigInteger(maxdigits, 16);
			
			publickey.put("e", eInt);
			publickey.put("n", nInt);
			publickey.put("maxidigits", maxInt);
			return publickey;
		}
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
