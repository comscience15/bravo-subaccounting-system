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

import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.util.BravoAuthenticationException;
import com.bravo.https.util.BravoStatus;

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
			ArrayList<JSONObject> cardList;
			try {
				cardList = ClientAPICalls.getCardListByCustID(ip, context);
				CardListDAO cardListDAO = new CardListDAO(context);
				cardListDAO.openDB();
				cardListDAO.insertCards(cardList);
				cardListDAO.closeDB();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (BravoAuthenticationException e) {
				logger.log(Level.WARNING, e.getMessage());
				return BravoStatus.OPERATION_FAILED;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return BravoStatus.OPERATION_SUCCESS;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result.equals(BravoStatus.OPERATION_FAILED)) {
			new BravoAlertDialog(context).showDialog("Login Failed", "You did not login yet, please login first.", "OK");
		} 
	}
}
