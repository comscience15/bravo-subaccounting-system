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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
/**
 * This class is implementing async login, running in background
 * @author Daniel
 * @email danniel1205@gmail.com
 */
public class AsyncLogin extends AsyncTask<String, Void, String>{
	public static Encryption EncryptionObj;
	private Context context;
	public AsyncLogin(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... loginInfo) {

		final String username = loginInfo[0];
		final String password = loginInfo[1];
		final String roletype = loginInfo[2];
		final String domain = loginInfo[3];
		final String ip = loginInfo[4];
		
		String loginStatus = null;
		try {
			loginStatus = CommonAPICalls.login(username, password, roletype, domain, ip, context);
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
		}//APICallsFactory.login(loginInfo[0], loginInfo[1], loginInfo[2], context);
		
		// After login, we should get the public key at the same time 
		EncryptionObj = new Encryption("Test", context);
		EncryptionObj.getPublicKey(ip);
		
		return loginStatus;
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		/** if login successfully, forward to Main activity temporarily**/
		if (result != null && !result.equals("404")) {
			
			
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "Login");
			toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			/** if login unsuccessfully, showing the alert dialog**/
			System.out.println("Debugging for showing login dialog"); // this should be removed
			new BravoAlertDialog(context).showDialog("Login Failed", "Please check your authentication or network connection", "OK");
		}
	}
}
