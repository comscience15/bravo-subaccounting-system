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
import com.bravo.bravoclient.util.Encryption;
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
 * This class is implementing async login, running in background
 * @author Daniel
 * @email danniel1205@gmail.com
 */
public class AsyncLogin extends BasicAsyncTask{
	public static Encryption EncryptionObj;
	private static Logger logger = Logger.getLogger(AsyncLogin.class.getName());
	
	public AsyncLogin(Context context) {
		super(context, context.getString(R.string.async_login));
	}
	
	@Override
	protected String doInBackground(String... loginInfo) {
		final String username = loginInfo[0];
		final String password = loginInfo[1];
		final String roletype = loginInfo[2];
		final String domain = loginInfo[3];
		final String ip = loginInfo[4];
		
		// Do login in new thread, in order to do the real time watching for the response
		new Thread(new Runnable() {
			@Override
			public void run() {
				doLogin(username, password, roletype, domain, ip);
			}
		}).start();
		
		postProgress();
		
		if (jsonResponse != null && jsonResponse.equals("") == false) {
			// After login, we should get the public key at the same time 
			EncryptionObj = new Encryption(context);
			EncryptionObj.getPublicKey(ip);
		}
		return jsonResponse;
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		/** if login successfully, forward to Main activity temporarily**/
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status == null) return; // Connection timeout, just return
		if (status.equals(BravoStatus.OPERATION_SUCCESS) == true) {
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "Login");
			toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			new BravoAlertDialog(context).showDialog("Login Failed", msg, "OK");
		}
	}
	
	// Doing the really login here
	private void doLogin(String username, String password, String roletype, String domain, String ip) {
		try {
			jsonResponse = CommonAPICalls.login(username, password, roletype, domain, ip, context);
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
