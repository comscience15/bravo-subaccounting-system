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
import com.bravo.bravoclient.model.Card;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.https.apicalls.CommonAPICalls;
import com.bravo.https.util.HttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * This Class is the implementations of async logout in background
 * @author Daniel
 * @email danniel1205@gmail.com
 */
public class AsyncLogout extends AsyncTask<String, Integer, String>{
	private Context context;
	private static ProgressDialog progressDialog; 
	private static String logoutResponse;
	private static final Logger logger = Logger.getLogger(AsyncLogout.class.getName());
	public AsyncLogout(Context context) {
		this.logoutResponse = "";
		this.context = context;
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Now Logout......");
		progressDialog.setMax(100);
	}
	/**
	 * This method is going to execute the async task
	 */
	@Override
	protected String doInBackground(String... logoutInfo) {
		final String ip = logoutInfo[0];
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				doLogout(ip);
			}
		}).start();
		
		postProgress();
		
		return logoutResponse;
	}
	
	protected void onProgressUpdate(Integer...sec) {
		progressDialog.setProgress(sec[0]);
		if (sec[0] == 0) {
			progressDialog.show();
		} else if (sec[0] == -1) {
			progressDialog.dismiss();
		} else if (sec[0] == Integer.MAX_VALUE) {
			progressDialog.dismiss();
			Toast.makeText(context, "Logout timeout", Toast.LENGTH_LONG).show();
		}
    }
	
	/**
	 * This method is going to handle the execution result
	 */
	@Override
	protected void onPostExecute(String result) {
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status != null && (status.equals("404") == false)) {
			
			//Clean up local db
			CardListDAO cardListDAO = new CardListDAO(context);
			cardListDAO.openDB();
			cardListDAO.deleteAllCards();
			cardListDAO.closeDB();
			
			CharSequence text = context.getString(R.string.logout_toast);
	    	int duration = Toast.LENGTH_SHORT;
	    	Toast.makeText(context, text, duration).show();
			
	    	MainActivity.setLoginStatus(false);
		} else {
			new BravoAlertDialog(context).showDialog("Logout Failed", msg, "OK");
			MainActivity.setLoginStatus(true);
		}
	}
	
	private void doLogout(String ip) {
		try {
			logoutResponse = CommonAPICalls.logout(ip, context);
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
	
	private void postProgress() {
		int sec = 0;
		int timeout = Integer.valueOf(context.getString(R.string.network_timeout));
		while (logoutResponse == null || logoutResponse.equals("")) {
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
		// if network response is got by api call, post -1 to onProgressUpdate
		publishProgress(-1);
	}

}
