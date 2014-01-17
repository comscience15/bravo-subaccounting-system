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
import com.bravo.https.apicalls.CommonAPICalls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * This Class is the implementations of async logout in background
 * @author Daniel
 * @email danniel1205@gmail.com
 */
public class AsyncLogout extends AsyncTask<String, Void, String>{
	private Context context;
	public AsyncLogout(Context context) {
		this.context = context;
	}
	/**
	 * This method is going to execute the async task
	 */
	@Override
	protected String doInBackground(String... logoutInfo) {
		final String ip = logoutInfo[0];
		String logoutStatus = null;
		try {
			logoutStatus = CommonAPICalls.logout(ip, context);
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
		return logoutStatus;
	}
	
	/**
	 * This method is going to handle the execution result
	 */
	@Override
	protected void onPostExecute(String result) {
		if (result != null && (result.equals("404") == false)) {
			new BravoAlertDialog(context).showDialog("Logout Successfully", "You are logged out now, you might have to login again for future operations", "OK");
			MainActivity.setLoginStatus(false);
			
			Intent toMainActivity = new Intent(context, MainActivity.class);
			toMainActivity.putExtra("Activity", "Logout");
			toMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toMainActivity);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			new BravoAlertDialog(context).showDialog("Logout Failed", "Please check your authentication or network connection", "OK");
			MainActivity.setLoginStatus(true);
		}
	}

}
