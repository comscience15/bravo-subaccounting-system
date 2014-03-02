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

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.util.BravoAuthenticationException;
import com.bravo.https.util.BravoStatus;
import com.bravo.https.util.HttpResponseHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
			String jsonResponse;
			try {
				jsonResponse = ClientAPICalls.getCardListByCustID(ip, context);
				String status = HttpResponseHandler.parseJson(jsonResponse, "status");
				String msg = HttpResponseHandler.parseJson(jsonResponse, "message");
				if (jsonResponse != null && status.equals(BravoStatus.OPERATION_SUCCESS)) {
					cardList = HttpResponseHandler.toArrayList(msg);
					CardListDAO cardListDAO = new CardListDAO(context);
					cardListDAO.openDB();
					cardListDAO.insertCards(cardList);
					cardListDAO.closeDB();
				} else if (jsonResponse != null && status.equals(BravoStatus.OPERATION_NO_CONTENT_RESPONSE)) {
					return BravoStatus.OPERATION_NO_CONTENT_RESPONSE;
				} else {
					return BravoStatus.OPERATION_FAILED;
				}
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
			String msg = context.getResources().getString(R.string.failure_get_card_list);
			new BravoAlertDialog(context).showDialog("Get Card List Failed.", msg, "OK");
		} else if (result.equals(BravoStatus.OPERATION_NO_CONTENT_RESPONSE)) {
			Toast.makeText(context, "Currently have not cards found", Toast.LENGTH_LONG).show();
		}
	}
}
