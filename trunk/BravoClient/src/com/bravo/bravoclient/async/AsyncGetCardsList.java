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
import com.bravo.bravoclient.activities.CardsListActivity;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.model.Card;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.util.BravoAuthenticationException;
import com.bravo.https.util.BravoStatus;
import com.bravo.https.util.HttpResponseHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AsyncGetCardsList extends AsyncTask<String, Integer, String>{
	private Context context;
	private View view;
	private static String jsonResponse;
	private Logger logger = Logger.getLogger(AsyncGetCardsList.class.getName());
	private static ProgressDialog progressDialog;
	
	public AsyncGetCardsList(Context context, View view) {
		this.context = context;
		this.view = view;
		this.jsonResponse = "";
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Loading cards......");
		progressDialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		final String ip = params[0];
		new Thread(new Runnable() {
			@Override
			public void run() {
				doGetCardListByCusID(ip);
			}
		}).start();
		postProgress();
		return jsonResponse;
	}
	
	protected void onProgressUpdate(Integer... progress) {
		progressDialog.setProgress(progress[0]);
		if (progress[0] == 0) {
			progressDialog.show();
		} else if (progress[0] == -1 ) {
			progressDialog.dismiss();
		} else if (progress[0] == Integer.MAX_VALUE) {
			progressDialog.dismiss();
			Toast.makeText(context, "Get card list timeout", Toast.LENGTH_SHORT).show();
			return;
		}
    }

	@Override
	protected void onPostExecute(String result) {
		ArrayList<JSONObject> cardList = null;
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status != null && status.equals(BravoStatus.OPERATION_SUCCESS)) {
			try {
				cardList = HttpResponseHandler.toArrayList(msg);
			} catch (JSONException e) {
				logger.severe(e.getMessage());
			}
			CardListDAO cardListDAO = new CardListDAO(context);
			cardListDAO.openDB();
			cardListDAO.insertCards(cardList);
			cardListDAO.closeDB();
			updateBalance();
		} else if (status != null && status.equals(BravoStatus.OPERATION_NO_CONTENT_RESPONSE)) {
			Toast.makeText(context, "Currently have not cards found", Toast.LENGTH_LONG).show();
		} else {
			String msg1 = context.getString(R.string.failure_get_card_list);
			new BravoAlertDialog(context).showDialog("Get card list", msg1, "OK");
		}
	}
	
	private void postProgress() {
		int sec = 0;
		int timeout = Integer.valueOf(context.getString(R.string.network_timeout));
		while (jsonResponse == null || jsonResponse.equals("")) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.severe("Timer has been stopped");
			}
			publishProgress(sec);
			sec ++;
			if (sec >= timeout) {
				publishProgress(Integer.MAX_VALUE);
			}
		}
		// if network response is gotten by api call, post -1 to onProgressUpdate
		publishProgress(-1);
	}
	
	private void doGetCardListByCusID(String ip) {
		try {
			jsonResponse = ClientAPICalls.getCardListByCustID(ip, context);
		} catch (KeyManagementException e) {
			logger.severe(e.getMessage());
		} catch (UnrecoverableKeyException e) {
			logger.severe(e.getMessage());
		} catch (CertificateException e) {
			logger.severe(e.getMessage());
		} catch (KeyStoreException e) {
			logger.severe(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.severe(e.getMessage());
		} catch (IOException e) {
			logger.severe(e.getMessage());
		} catch (JSONException e) {
			logger.severe(e.getMessage());
		} catch (BravoAuthenticationException e) {
			logger.severe(e.getMessage());
		}
	}
	
	private void updateBalance() {
		CardListDAO cardListDAO = new CardListDAO(context);
		SharedPreferences settings = context.getSharedPreferences(CardsListActivity.CHOOSE_CARD, context.MODE_PRIVATE);
		int selectedCardRowId = settings.getInt("SELECTED_CARD", 0);
		
		cardListDAO.openDB();
		Card card = cardListDAO.getCard(selectedCardRowId);
		cardListDAO.closeDB();
		
		EditText cardBalanceEditText = (EditText)view.findViewById(R.id.cardBalanceEditText);
		cardBalanceEditText.setText(context.getString(R.string.currency) + card.getBalance());
	}
}
