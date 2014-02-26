package com.bravo.https.apicalls;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.https.util.BravoAuthenticationException;
import com.bravo.https.util.BravoHttpsClient;
import com.bravo.https.util.CookieHandler;
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
	public static String getPublicKey(String IP, Context androidContext) 
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		final String ip = IP;
		final String path = "service/customer/transaction/getKey";
		final String URL = ip + path;
		
		String cookie = CookieHandler.getCookie(androidContext);
		
		logger.log(Level.INFO, "getKey: Cookie is :" + cookie);
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, null, cookie, "getKey", androidContext);
		
		return HttpResponseHandler.toString(response);
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
	 * @throws AuthenticationException 
	 * @throws org.apache.http.auth.AuthenticationException 
	 */
	public static String getCardListByCustID(String IP, Context androidContext) 
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, JSONException, BravoAuthenticationException {
		final String ip = IP;
		final String path = "service/customer/account/getCardListByCustID";
		final String URL = ip + path;
		
		String cookie = CookieHandler.getCookie(androidContext);
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, null, cookie, "getCardList", androidContext);
		
		
		return HttpResponseHandler.toString(response);//HttpResponseHandler.toArrayList(response);	
	}
	
	/**
	 * Load money by using credit card
	 * @param IP
	 * @param androidContext
	 * @param firstName
	 * @param middleInitial
	 * @param lastName
	 * @param accountNumber
	 * @param cvn
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param totalAmount
	 * @param cardID
	 * @param expirationDate
	 * @param boolean saveProfile
	 * @param boolean existProfile
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	public static String reloadMoneyByCreditCard(String IP, Context androidContext, ArrayList<NameValuePair> paraList) 
					throws IOException, KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
		final String ip = IP;
		final String path = "/service/customer/transaction/loadMoney";
		final String URL = ip + path;
		String cookie = CookieHandler.getCookie(androidContext);
		
		HttpResponse response = BravoHttpsClient.doHttpsPost(URL, paraList, cookie, "loadMoneyByCreditCard", androidContext);
		
		String JSONString = HttpResponseHandler.toString(response);
		
		logger.info(JSONString);
		
		return JSONString;
	}
}
