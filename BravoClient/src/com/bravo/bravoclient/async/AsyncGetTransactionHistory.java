package com.bravo.bravoclient.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.activities.TransactionListActivity;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.util.BravoStatus;
import com.bravo.https.util.HttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncGetTransactionHistory extends AsyncTask<String, Integer, String>{

	private Context context;
	private static String jsonResponse;
	private static ProgressDialog progressDialog;
	private Logger logger = Logger.getLogger(AsyncGetTransactionHistory.class.getName());
	
	public AsyncGetTransactionHistory(Context context) {
		this.context = context;
		this.jsonResponse = "";
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Loading transaction history......");
		progressDialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		final String ip = params[0];
		final String cardID = params[1];
		new Thread(new Runnable() {
			@Override
			public void run() {
				doGetTransactionHistory(ip, cardID);
			}
		}).start();
		postProgress();
		return jsonResponse;
	}
	
	@Override
	protected void onPostExecute(String result) {
		ArrayList<JSONObject> transactionList;
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status != null && status.equals(BravoStatus.OPERATION_SUCCESS)) {
			Intent toTransactionListActivity = new Intent(context, TransactionListActivity.class);
			toTransactionListActivity.putExtra("jsonResponse", msg);
			toTransactionListActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toTransactionListActivity);
	    	// TODO: We should change the animation later
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else if (status != null && status.equals(BravoStatus.OPERATION_NO_CONTENT_RESPONSE)) {
			Toast.makeText(context, "Currently have not transactions found", Toast.LENGTH_LONG).show();
		} else {
			String msg1 = context.getString(R.string.failure_get_transaction_history);
			new BravoAlertDialog(context).showDialog("Get transaction history", msg1, "OK");
		}
	}
		
	
	private void doGetTransactionHistory(String ip, String cardID) {
		try {
			jsonResponse = ClientAPICalls.getTransactionHistory(ip, context, cardID);
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
		}
		
	}
	
	protected void onProgressUpdate(Integer... progress) {
		progressDialog.setProgress(progress[0]);
		if (progress[0] == 0) {
			progressDialog.show();
		} else if (progress[0] == -1 ) {
			progressDialog.dismiss();
		} else if (progress[0] == Integer.MAX_VALUE) {
			progressDialog.dismiss();
			Toast.makeText(context, "Get transaction list timeout", Toast.LENGTH_SHORT).show();
			return;
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

}
