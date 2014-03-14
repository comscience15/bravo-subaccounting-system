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
import com.bravo.https.util.BravoStatus;
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
public class AsyncLogout extends BasicAsyncTask{
	private static final Logger logger = Logger.getLogger(AsyncLogout.class.getName());
	public AsyncLogout(Context context) {
		super(context, context.getString(R.string.async_logout));
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
		
		return jsonResponse;
	}
	
	/**
	 * This method is going to handle the execution result
	 */
	@Override
	protected void onPostExecute(String result) {
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status == null) return;
		if (status.equals(BravoStatus.OPERATION_SUCCESS) == true) {
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
			jsonResponse = CommonAPICalls.logout(ip, context);
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
