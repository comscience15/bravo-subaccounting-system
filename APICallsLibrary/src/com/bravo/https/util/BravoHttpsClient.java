package com.bravo.https.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;

public class BravoHttpsClient {
	
	public static DefaultHttpClient createHttpsClient(Context androidContext) throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
		
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

}
