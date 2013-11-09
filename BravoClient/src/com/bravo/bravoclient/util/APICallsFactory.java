package com.bravo.bravoclient.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

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
	private static HttpResponse doHttpPost(String URL, List<NameValuePair> nameValuePair, String cookie, String APIName, Context requestContext) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		
		if (cookie != null)	httpPost.setHeader("Cookie", cookie);
		
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
			HttpResponse response = httpClient.execute(httpPost);
			
			if (APIName.equals("login") || APIName.equals("register")) {
			    // Get the cookies from server
			    List<Cookie> cookies = httpClient.getCookieStore().getCookies();
			    String cookieValue = cookies.get(0).getName()+ "=" + cookies.get(0).getValue();
			  
			    System.out.println("Cookie Value is: " + cookieValue);
			  
			    FileOutputStream cookieFile = requestContext.openFileOutput("Cookie", Context.MODE_PRIVATE);
			    cookieFile.write(cookieValue.getBytes());
			    cookieFile.close();
			
//		      for (Cookie c : cookies) {
//		    	  System.err.println("Cookie is:\n Comment:" + c.getComment() +"\n CommentURL:" + c.getCommentURL() 
//		    		  	+"\n Domain:" + c.getDomain() +"\n Name:" + c.getName() +"\n Path:" + c.getPath() +"\n Value:" + c.getValue()
//		    			+"\n Version:" + c.getVersion() +"\n");
//		      }
			  
			}
		    
			return response;
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
	public static String login(String username, String password, String IP, Context cookieContext) {
		final String ip = IP; //context.getString(R.string.IP_Address);
		final String path = "service/authentication/j_spring_security_check";
		final String URL = ip + path;
		
		/**Creating parameters*/
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("j_username", username));
		nameValuePair.add(new BasicNameValuePair("j_password", password));
		nameValuePair.add(new BasicNameValuePair("j_domain", "200"));
		nameValuePair.add(new BasicNameValuePair("j_roletype", "customer"));
		
		HttpResponse response = doHttpPost(URL, nameValuePair, null, "login", cookieContext);
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
	 * @param IP
	 * @return
	 */
	public static String register(String username, String password, String street, String city, String state, String zipCode, String IP, Context cookieContext) {
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
		
		HttpResponse response = doHttpPost(URL, nameValuePair, null, "register", cookieContext);
		
		String registerStatus = HttpResponseHandler.parseJson(response, "status");
		// loginStatus is null means login successfully, see API document for details
	    return registerStatus == null ? "200" : registerStatus;
	}
	
	/**
	 * Get public key API
	 * @param IP
	 * @return
	 */
	public static Hashtable<String, BigInteger> getPublicKey(String IP, Context requestContext) {
		final String ip = IP; //context.getString(R.string.IP_Address);
		final String path = "service/customer/transaction/getKey";
		final String URL = ip + path;
		
		FileInputStream getStoredCookie = null;
		byte[] cookieByte = null;
		int cookieSize = 0;
		
		try {
			getStoredCookie = requestContext.openFileInput("Cookie");
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
		
		HttpResponse response = doHttpPost(URL, null, cookie, "getKey", null);
		
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
