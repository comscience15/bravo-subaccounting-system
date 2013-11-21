package com.bravo.https.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ssl.SSLSocketFactory;


import android.content.Context;

/**
 * This is the class where we update our trus keystore and initiate Apache SSLSocketFacotry
 * @author Daniel
 *
 */
public class BravoSSLSocketFactory {
	SSLSocketFactory sslSocketFactory;
	
	public BravoSSLSocketFactory(Context androidContext) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException, UnrecoverableKeyException {
		KeyStore trustStore = createTrustStore(androidContext);
		this.sslSocketFactory = new SSLSocketFactory(trustStore);
	}
	
	/**
	 * Get the customized SSLSocketFactory
	 * @return
	 */
	public SSLSocketFactory getSSLSocketFactory() {
		return sslSocketFactory;
	}
	
	/**
	 * Create trust store from file
	 * @param androidContext
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException 
	 */
	private KeyStore createTrustStore(Context androidContext) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		// Load CAs from certificate file
		CertificateFactory certificateFacotry = CertificateFactory.getInstance("X.509");
		// Get the certificate file from local assets folder
		InputStream certificateInput = androidContext.getAssets().open("trust.crt");
		// Generate the certificate
		Certificate certificate = certificateFacotry.generateCertificate(certificateInput);
		
		// Generate the trust store which contains our CAs from server;
		String keyStoreType = KeyStore.getDefaultType(); // default type is BKS
		KeyStore trustStore = KeyStore.getInstance(keyStoreType);
		trustStore.load(null, null);
		trustStore.setCertificateEntry("bravo_certificate", certificate);
		 
		System.out.println("If there is a certificate for bravo: " + trustStore.isCertificateEntry("bravo_certificate")); // debug
		System.out.println("Certificate is: " + trustStore.getCertificate("bravo_certificate").toString()); // debug

		// Initiate the trust store by using trust manager factory
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		tmf.init(trustStore);
		
		// Create an SSLContext that uses our TrustManager
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, tmf.getTrustManagers(), null);
		
		return trustStore;
		
	}

}
