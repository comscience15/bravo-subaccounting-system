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
 *
 */
public class AsyncRegister extends BasicAsyncTask{
	public static Encryption EncryptionObj;
	private static final Logger logger = Logger.getLogger(AsyncRegister.class.getName());
	
	public AsyncRegister(Context context) {
		super(context, context.getString(R.string.async_register));
	}
	
	@Override
	protected String doInBackground(String... registerInfo) {
		final String username = registerInfo[0];
		final String password = registerInfo[1];
		final String street = registerInfo[2];
		final String city = registerInfo[3];
		final String state = registerInfo[4];
		final String zipCode = registerInfo[5];
		final String roleType = registerInfo[6];
		final String domain = registerInfo[7];
		final String ip = registerInfo[8];
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				doRegister(username, password, street, city, state, zipCode, roleType, domain, ip);
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
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		/** if register successfully, forward to Main activity temporarily**/
		if (status != null && !status.equals("404")) {
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "Register");
			toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			/** if register unsuccessfully, showing the alert dialog**/
			new BravoAlertDialog(context).showDialog("Registeration Failed", msg, "OK");
		}
	}
	
	private void doRegister(String username, String password, String street, String city, String state, String zipCode, String roleType, String domain, String ip) {
		try {
			jsonResponse = CommonAPICalls.register(username, password, street, city, state, zipCode, roleType, domain, ip, context);
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
