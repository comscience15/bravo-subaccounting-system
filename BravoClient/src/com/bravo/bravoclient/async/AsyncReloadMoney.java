package com.bravo.bravoclient.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.CardsListActivity;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.util.HttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Async task for reloading money
 * This class did not extends the basic async task, is because we are passing object into this class
 * @author daniel
 *
 */
public class AsyncReloadMoney extends AsyncTask<Object, Integer, String>{
	private Context context;
	private static String jsonResponse;
	private static ProgressDialog progressDialog;
	private static final Logger logger = Logger.getLogger(AsyncReloadMoney.class.getName());
	
	public AsyncReloadMoney(Context context) {
		this.context = context;
		this.jsonResponse = "";
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Now reloading money......");
		progressDialog.setMax(100);
	}
	
	/**
	 * params[0] is IP address
	 * params[1] is parameters for api call
	 */
	@Override
	protected String doInBackground(Object... params) {
		final String ip;
		final ArrayList<NameValuePair> apiParaList;
		
		if (params[1] instanceof ArrayList && params[0] instanceof String) {
			apiParaList = (ArrayList<NameValuePair>) params[1];
			ip = (String) params[0];
		} else {
			logger.warning("Parameters for api call are invalid");
			return null;
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				doReloadMoneyByCreditCard(ip, apiParaList);
			}
		}).start();
		
		postProgress();
		
		return jsonResponse;
	}
	
	protected void onProgressUpdate(Integer...sec) {
		progressDialog.setProgress(sec[0]);
		if (sec[0] == 0) {
			progressDialog.show();
		} else if (sec[0] == -1) {
			progressDialog.dismiss();
		} else if (sec[0] == Integer.MAX_VALUE) {
			progressDialog.dismiss();
			Toast.makeText(context, "Reload money timeout", Toast.LENGTH_LONG).show();
		}
    } 
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status != null && (status.equals("404")) == false) {
			double newBalance = Double.valueOf(msg);
			SharedPreferences settings = context.getSharedPreferences(CardsListActivity.CHOOSE_CARD, context.MODE_PRIVATE);
			int RowID = settings.getInt("SELECTED_CARD", 0);
			
			CardListDAO cardListDAO = new CardListDAO(context);
			cardListDAO.openDB();
			cardListDAO.updateCardBalance(RowID, newBalance);
			cardListDAO.closeDB();
			
			String msgToast = MessageFormat.format("Reloading card successful. New balance is $ {0}", newBalance);
			Toast.makeText(context,msgToast, Toast.LENGTH_SHORT).show();
			
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "Reload_Money");
			toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			new BravoAlertDialog(context).showDialog("Reload Money Failed", msg, "OK");
		}
	}
	
	private void doReloadMoneyByCreditCard(String ip, ArrayList<NameValuePair> apiParaList) {
		try {
			jsonResponse = ClientAPICalls.reloadMoneyByCreditCard(ip, context, apiParaList);
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
				break;
			}
		}
		// if network response is got by api call, post -1 to onProgressUpdate
		if (jsonResponse != null && jsonResponse.equals("") == false) publishProgress(-1);
	}

}
