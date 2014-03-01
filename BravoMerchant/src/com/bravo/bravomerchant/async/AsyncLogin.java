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
import com.bravo.https.util.HttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
/**
 * This class is implementing async login, running in background
 * @author jiawl
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

			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {

			e.printStackTrace();
		} catch (CertificateException e) {

			e.printStackTrace();
		} catch (KeyStoreException e) {

			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return loginStatus;
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		
		/** if login successfully, forward to Main activity temporarily**/
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		
		if (status != null && !status.equals("404")) {
			
			Intent toMainActivity = new Intent(context, MainActivity.class);
			toMainActivity.putExtra("Activity", "Login");
	    	context.startActivity(toMainActivity);
	    	((Activity) context).overridePendingTransition(com.bravo.bravomerchant.R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			
			/** if login unsuccessfully, showing the alert dialog**/
			new BravoAlertDialog(context).showDialog("Login Failed", msg, "OK");
		}
		
	}
	
}
