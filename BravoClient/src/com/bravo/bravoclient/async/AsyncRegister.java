package com.bravo.bravoclient.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.util.Encryption;
import com.bravo.https.apicalls.CommonAPICalls;
import com.bravo.https.util.HttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
/**
 * This class is implementing async login, running in background
 * @author Daniel
 * @email danniel1205@gmail.com
 *
 */
public class AsyncRegister extends AsyncTask<String, Void, String>{
	private Context context;
	public static Encryption EncryptionObj;
	public AsyncRegister(Context context) {
		this.context = context;
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
		
		String registerResponse = null;
		
		try {
			registerResponse = CommonAPICalls.register(username, password, street, city, state, zipCode, roleType, domain, ip, context);
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
		
		// After login, we should get the public key at the same time 
		EncryptionObj = new Encryption(context);
		EncryptionObj.getPublicKey(ip);
		return registerResponse;
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
			new BravoAlertDialog(context).showDialog("Register Failed", msg, "OK");
		}
	}
}
