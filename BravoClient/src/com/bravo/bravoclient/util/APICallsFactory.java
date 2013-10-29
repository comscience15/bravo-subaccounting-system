package com.bravo.bravoclient.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * This class will handle and manage all api calls to server
 * @author Daniel
 *
 */
public class APICallsFactory {
	public APICallsFactory() {
		
	}
	
	/**
	 * This method is going to execute the basic http post request with parameters
	 * @param URL
	 * @param nameValuePair: the parameters
	 * @return
	 */
	private static HttpResponse doHttpPost(String URL, List<NameValuePair> nameValuePair) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		if (nameValuePair !=  null) {
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				System.err.println("Encoding Parameter Exception: " + e.toString());
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			return httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			System.err.println("Client Protocol Exception: " + e.toString());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("IO Exception: " + e.toString());
			e.printStackTrace();
			return null;
		}
		
	} 
	
	/**
	 * Login API Call
	 * @param username
	 * @param password
	 * @param IP
	 * @return
	 */
	public static String login(String username, String password, String IP) {
		final String ip = IP; //context.getString(R.string.IP_Address);
		final String path = "service/authentication/j_spring_security_check";
		final String URL = ip + path;
		
		/**Creating parameters*/
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("j_username", username));
		nameValuePair.add(new BasicNameValuePair("j_password", password));
		nameValuePair.add(new BasicNameValuePair("j_domain", "200"));
		nameValuePair.add(new BasicNameValuePair("j_roletype", "customer"));
		
		HttpResponse response = doHttpPost(URL, nameValuePair);
		String loginStatus = new HttpResponseHandler().parseJson(response, "status");
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
	 * @param IP
	 * @return
	 */
	public static String register(String username, String password, String street, String city, String state, String zipCode, String IP) {
		final String ip = IP; //context.getString(R.string.IP_Address);
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
		nameValuePair.add(new BasicNameValuePair("j_domain", "200"));
		nameValuePair.add(new BasicNameValuePair("j_roletype", "customer"));
		
		HttpResponse response = doHttpPost(URL, nameValuePair);
		
		String registerStatus = new HttpResponseHandler().parseJson(response, "status");
		// loginStatus is null means login successfully, see API document for details
	    return registerStatus == null ? "200" : registerStatus;
	}
	
	/**
	 * Get public key API
	 * @param IP
	 * @return
	 */
	public static Hashtable<String, BigInteger> getPublicKey(String IP) {
		final String ip = IP; //context.getString(R.string.IP_Address);
		final String path = "service/customer/transaction/getkey";
		final String URL = ip + path;
		
		HttpResponse response = doHttpPost(URL, null);
		HttpResponseHandler responseHandler = new HttpResponseHandler();
		String e = responseHandler.parseJson(response, "e");
		String n = responseHandler.parseJson(response, "n");
		String maxdigits = responseHandler.parseJson(response, "maxdigits");
		
		System.out.println("e: " + e + " n: " + n + "max:" + maxdigits);
		
		BigInteger eInt = new BigInteger(e, 16);
		BigInteger nInt = new BigInteger(n, 16);
		BigInteger maxInt = new BigInteger(maxdigits, 16);
		
		Hashtable<String, BigInteger> publickey = new Hashtable<String, BigInteger>();
		publickey.put("e", eInt);
		publickey.put("n", nInt);
		publickey.put("maxidigits", maxInt);
		
		return publickey;
	}

}
