package com.bravo.https.apicalls;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bravo.https.util.BravoHttpsClient;
import com.bravo.https.util.CookieHandler;
import com.bravo.https.util.HttpResponseHandler;

import android.content.Context;

public class CommonAPICalls {
	private static Logger logger = Logger.getLogger(CommonAPICalls.class.getName());
	protected static final String NO_CONNECTION = "{\"status\": \"404\", \"message\": \"No Network Connection. Please connect your phone to network first.\"}";
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
		
		if (BravoHttpsClient.checkNetworkConnectivity(androidContext) == false) {
			return CommonAPICalls.NO_CONNECTION;
		}
		
		/**Creating parameters*/
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("j_username", username));
		nameValuePair.add(new BasicNameValuePair("j_password", password));
		nameValuePair.add(new BasicNameValuePair("j_domain", domain));
		nameValuePair.add(new BasicNameValuePair("j_roletype", roleType));
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, nameValuePair, null, "login", androidContext);
		return HttpResponseHandler.toString(response);
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
		
		if (BravoHttpsClient.checkNetworkConnectivity(androidContext) == false) {
			return CommonAPICalls.NO_CONNECTION;
		}
		
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
		
	    return HttpResponseHandler.toString(response);
	}
	
	/**
	 * Logout API Call
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
	public static String logout(String IP, Context androidContext) 
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		final String ip = IP;
		final String path = "service/authentication/j_spring_security_logout";
		final String URL = ip + path;
		
		if (BravoHttpsClient.checkNetworkConnectivity(androidContext) == false) {
			return CommonAPICalls.NO_CONNECTION;
		}
		
		String cookie = CookieHandler.getCookie(androidContext);
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, null, cookie, "logout", androidContext);
		return HttpResponseHandler.toString(response);
	}
	
}
