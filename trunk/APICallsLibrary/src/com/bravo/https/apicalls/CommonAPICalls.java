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
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bravo.https.util.BravoHttpsClient;
import com.bravo.https.util.HttpResponseHandler;

import android.content.Context;

public class CommonAPICalls {
	
	/**
	 * Login API Call
	 * @param username
	 * @param password
	 * @param roleType
	 * @param domain by defult it is "200"
	 * @param IP
	 * @param androidContext
	 * @return
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String login(String username, String password, String roleType, String domain, String IP, Context androidContext) 
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		final String ip = IP;
		final String path = "service/authentication/j_spring_security_check";
		final String URL = ip + path;
		
		/**Creating parameters*/
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("j_username", username));
		nameValuePair.add(new BasicNameValuePair("j_password", password));
		nameValuePair.add(new BasicNameValuePair("j_domain", domain));
		nameValuePair.add(new BasicNameValuePair("j_roletype", roleType));
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, nameValuePair, null, "login", androidContext);
		String loginStatus = HttpResponseHandler.parseJson(response, "status");
		// loginStatus is null means login successfully, see API document for details
	    return loginStatus == null ? "200" : loginStatus;
	}
	
	
	/**
	 * Register API Call
	 * @param username
	 * @param password
	 * @param street
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param roleType
	 * @param domain
	 * @param IP
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws UnrecoverableKeyException 
	 * @throws KeyManagementException 
	 */
	public static String register(String username, String password, String street, String city, String state, String zipCode, 
			 String roleType, String domain, String IP, Context androidContext) 
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		final String ip = IP;
		final String path = "service/authentication/signup";
		final String URL = ip + path;
		
		/**Creating parameters*/
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("j_username", username));
		nameValuePair.add(new BasicNameValuePair("j_password", password));
		nameValuePair.add(new BasicNameValuePair("street", street));
		nameValuePair.add(new BasicNameValuePair("city", city));
		nameValuePair.add(new BasicNameValuePair("state", state));
		nameValuePair.add(new BasicNameValuePair("zip", zipCode)); // Should change the back end to "zipCode"
		nameValuePair.add(new BasicNameValuePair("j_domain", domain));
		nameValuePair.add(new BasicNameValuePair("j_roletype", roleType));
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, nameValuePair, null, "register", androidContext);
		
		String registerStatus = HttpResponseHandler.parseJson(response, "status");
		// loginStatus is null means login successfully, see API document for details
	    return registerStatus == null ? "200" : registerStatus;
	}
	
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
		
		System.out.println("getKey: Cookie is :" + cookie); // Debug
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, null, cookie, "getKey", androidContext);
		
		String responseString = HttpResponseHandler.toString(response);
		String getKeyStatus = HttpResponseHandler.parseJson(responseString, "status");
		
		Hashtable<String, BigInteger> publickey = new Hashtable<String, BigInteger>();
		
		// Check if authentication is needed for getting keys
		if (getKeyStatus != null && getKeyStatus.equals("404")) {
			// Should go to login first
			System.err.println("Should go to login first");
			publickey.put("status", BigInteger.valueOf(404));
			return null;
		} else {
			String e = HttpResponseHandler.parseJson(responseString, "e");
			String n = HttpResponseHandler.parseJson(responseString, "n");
			String maxdigits = HttpResponseHandler.parseJson(responseString, "maxdigits");
		
			System.out.println("e: " + e + " n: " + n + " max:" + maxdigits);
		
			BigInteger eInt = new BigInteger(e, 16);
			BigInteger nInt = new BigInteger(n, 16);
			BigInteger maxInt = new BigInteger(maxdigits, 16);
			
			publickey.put("e", eInt);
			publickey.put("n", nInt);
			publickey.put("maxidigits", maxInt);
			return publickey;
		}
	}
	

}
