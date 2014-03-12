package com.bravo.bravoclient.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.util.BravoStatus;
import com.bravo.https.util.HttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncSendMoney extends AsyncTask<String, Integer, String>{
	private Context context;
	private static String jsonResponse;
	private Logger logger = Logger.getLogger(AsyncSendMoney.class.getName());
	private static ProgressDialog progressDialog;
	
	public AsyncSendMoney(Context context) {
		this.context = context;
		this.jsonResponse = "";
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Sending money......");
		progressDialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		final String ip = params[0];
		final String senderCardID = params[1];
		final String senderNote = params[2]; 
		final String receiverEmail = params[3]; 
		final String encryptedReceiverInfo = params[4]; 
		final String totalAmount = params[5];
		new Thread(new Runnable() {
			@Override
			public void run() {
				doSendMoney(ip, senderCardID, senderNote, receiverEmail, encryptedReceiverInfo, totalAmount);
			}
		}).start();
		postProgress();
		return jsonResponse;
	}
	
	protected void onProgressUpdate(Integer...progress){
		progressDialog.setProgress(progress[0]);
		if (progress[0] == 0) {
			progressDialog.show();
		} else if (progress[0] == -1 ) {
			progressDialog.dismiss();
		} else if (progress[0] == Integer.MAX_VALUE) {
			progressDialog.dismiss();
			Toast.makeText(context, "Send money timeout", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status != null && status.equals(BravoStatus.OPERATION_SUCCESS)) {
			// To card page
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "Send_Money");
			toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else if (status != null && status.equals(BravoStatus.OPERATION_FAILED)) {
			String msg1 = context.getString(R.string.faulure_send_money);
			new BravoAlertDialog(context).showDialog("Send money", msg1, "OK");
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

	private void doSendMoney(String ip, String senderCardID, String senderNote, String receiverEmail, String encryptedReceiverInfo, String totalAmount) {
		try {
			jsonResponse = ClientAPICalls.sendMoney(ip, context, senderCardID, senderNote, receiverEmail, encryptedReceiverInfo, totalAmount);
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
	
}
