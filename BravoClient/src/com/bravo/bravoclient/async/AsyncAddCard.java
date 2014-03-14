package com.bravo.bravoclient.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.bravo.https.apicalls.ClientAPICalls;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncAddCard extends BasicAsyncTask{

	public AsyncAddCard(Context context) {
		super(context, "Adding new card");
	}
	
	@Override
	protected String doInBackground(String... params) {
		final String ip = params[0];
		final String cardID = params[1];
		final String securityCode = params[2];
		final String primaryCard = params[3];

		new Thread(new Runnable() {
			@Override
			public void run() {
				doAddCard(ip, cardID, securityCode, primaryCard);
			}
		}).start();
		
		postProgress();
		
		
		return null;
	}
	
	private void doAddCard(String ip, String cardID, String securityCode, String primaryCard) {
		try {
			ClientAPICalls.addCard(ip, context, cardID, securityCode, primaryCard);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
