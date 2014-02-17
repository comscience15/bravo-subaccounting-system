package com.bravo.bravomerchant.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.activities.MainActivity;
import com.bravo.bravomerchant.dialogs.BravoAlertDialog;
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
		}
		
		return loginStatus;
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		System.err.println("Result code is: " + result);
		/** if login successfully, forward to Main activity temporarily**/
		if (result != null && !result.equals("404")) {
			
			Intent toMainActivity = new Intent(context, MainActivity.class);
//			Intent toMainActivity = new Intent(context, OrderConfirmActivity.class);
			toMainActivity.putExtra("Activity", "Login");
	    	context.startActivity(toMainActivity);
	    	((Activity) context).overridePendingTransition(com.bravo.bravomerchant.R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			/** if login unsuccessfully, showing the alert dialog**/
			System.out.println("Debugging for showing login dialog"); // this should be removed
			new BravoAlertDialog(context).showDialog("Login Failed", "Please check your authentication or network connection", "OK");
		}
	}
	
}
