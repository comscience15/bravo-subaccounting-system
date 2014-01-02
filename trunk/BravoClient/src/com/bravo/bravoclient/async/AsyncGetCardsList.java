package com.bravo.bravoclient.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.https.apicalls.ClientAPICalls;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncGetCardsList extends AsyncTask<String, Void, String>{
	private Context context;
	private Logger logger = Logger.getLogger(AsyncGetCardsList.class.getName());
	public AsyncGetCardsList(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		final String ip = params[0];
		try {
			ArrayList<JSONObject> cardList = ClientAPICalls.getCardListByCustID(ip, context);
			CardListDAO cardListDAO = new CardListDAO(context);
			cardListDAO.openDB();
			cardListDAO.insertCards(cardList);
			cardListDAO.closeDB();
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO 
	}
}
