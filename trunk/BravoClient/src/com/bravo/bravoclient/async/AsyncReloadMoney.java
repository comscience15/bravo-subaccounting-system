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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Async task for reloading money
 * @author daniel
 *
 */
public class AsyncReloadMoney extends AsyncTask<Object, Void, Hashtable<String, String>>{
	private Context context;
	private static final Logger logger = Logger.getLogger(AsyncReloadMoney.class.getName());
	
	public AsyncReloadMoney(Context context) {
		this.context = context;
	}
	
	/**
	 * params[0] is IP address
	 * params[1] is parameters for api call
	 */
	@Override
	protected Hashtable<String, String> doInBackground(Object... params) {
		String ip;
		ArrayList<NameValuePair> apiParaList;
		Hashtable<String, String> APIResponse = new Hashtable<String, String>();
		if (params[1] instanceof ArrayList && params[0] instanceof String) {
			apiParaList = (ArrayList<NameValuePair>) params[1];
			ip = (String) params[0];
		} else {
			logger.warning("Parameters for api call are invalid");
			return null;
		}
		
		try {
			APIResponse = ClientAPICalls.reloadMoneyByCreditCard(ip, context, apiParaList);
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
		
		return APIResponse;
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(Hashtable<String, String> result) {
		if (result != null && (result.get("status").equals("404")) == false) {
			double newBalance = Double.valueOf(result.get("message"));
			SharedPreferences settings = context.getSharedPreferences(CardsListActivity.CHOOSE_CARD, context.MODE_PRIVATE);
			int RowID = settings.getInt("SELECTED_CARD", 0);
			
			logger.info("RowID is: " + RowID);
			logger.info("New Balance is: " + newBalance);
			
			CardListDAO cardListDAO = new CardListDAO(context);
			cardListDAO.openDB();
			cardListDAO.updateCardBalance(RowID, newBalance);
			cardListDAO.closeDB();
			
			String msg = MessageFormat.format("Reloading card successful. New balance is $ {0}", newBalance);
			Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
			
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "ReloadMoney");
			toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			new BravoAlertDialog(context).showDialog("Reload Money Failed", result.get("message"), "OK");
		}
	}

}
