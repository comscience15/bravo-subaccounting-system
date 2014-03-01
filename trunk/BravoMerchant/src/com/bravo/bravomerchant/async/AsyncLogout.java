package com.bravo.bravomerchant.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.bravo.bravomerchant.activities.LoginActivity;
import com.bravo.bravomerchant.activities.MainActivity;
import com.bravo.https.apicalls.CommonAPICalls;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * send logout request to the server and then turn to the login activity
 * @author wenlong_jia
 *
 */
public class AsyncLogout extends AsyncTask<String, Void, String>{
	
	private Context context;
	
	public AsyncLogout(Context context) {
		
		this.context = context;
	}
	
	/**
	 * send the logout request  
	 */
	@Override
	protected String doInBackground(String... logoutInfo) {
		
		final String ip = logoutInfo[0];
		String logoutStatus = null;
		
		try {
			
			logoutStatus = CommonAPICalls.logout(ip, context);
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
		return logoutStatus;
	}
	
	/**
	 * turn to the login activity, don't handle the response result
	 */
	@Override
	protected void onPostExecute(String result) {
		
		MainActivity.setLogin(false);
    	Intent toLoginActivity = new Intent(context, LoginActivity.class);
    	context.startActivity(toLoginActivity);
	}
}
