package com.bravo.https.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;

public class BravoHttpsClient {
	
	private static DefaultHttpClient createHttpsClient(Context androidContext) throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		
		// Set the https parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, false);
		
		// Set the socket factory, and allow all hostname verifier
		BravoSSLSocketFactory bsf = new BravoSSLSocketFactory(androidContext);
		SSLSocketFactory sf = bsf.getSSLSocketFactory();
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		 
		// Add customized port scheme to client connection manager
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
		schReg.register(new Scheme("https", sf, 8181));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		// Create and return new customized defaultHttpClient
		return new DefaultHttpClient(conMgr, params);
	}
	
	/**
	 * This method is going to execute the basic https post request with parameters
	 * @param URL
	 * @param nameValuePair: the parameters
	 * @return
	 */
	public static HttpResponse doHttpsPost(String URL, List<NameValuePair> nameValuePair, String cookie, String APIName, Context requestContext) {
		DefaultHttpClient httpsClient = null;
		try {
			httpsClient = BravoHttpsClient.createHttpsClient(requestContext);
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnrecoverableKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CertificateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpPost httpsPost = new HttpPost(URL);
		
		if (cookie != null)	httpsPost.setHeader("Cookie", cookie);
		
		if (nameValuePair !=  null) {
			try {
				httpsPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				System.err.println("Encoding Parameter Exception: " + e.toString());
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			HttpResponse response = httpsClient.execute(httpsPost);
			
			// Going to get the cookie only when login and register
			if (APIName.equals("login") || APIName.equals("register")) {
			    // Get the cookies from server
			    List<Cookie> cookies = httpsClient.getCookieStore().getCookies();
			    String cookieValue = cookies.get(0).getName()+ "=" + cookies.get(0).getValue();
			  
			    System.out.println("Cookie Value is: " + cookieValue);
			  
			    // Created the private local file which contains the cookie from server side
			    FileOutputStream cookieFile = requestContext.openFileOutput("Cookie", Context.MODE_PRIVATE);
			    
			    cookieFile.write(cookieValue.getBytes());
			    cookieFile.close();
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

}
