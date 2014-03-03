package com.bravo.bravoclient.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.dialogs.BravoProgressDialog;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.bravoclient.util.Encryption;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.apicalls.CommonAPICalls;
import com.bravo.https.util.BravoAuthenticationException;
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
public class AsyncLogin extends AsyncTask<String, Integer, String>{
	public static Encryption EncryptionObj;
	private Context context;
	private static int waitingTime = 1;
	private static Logger logger = Logger.getLogger(AsyncLogin.class.getName());
	private static ProgressDialog progressDialog;
	private static String ip;
	
	public AsyncLogin(Context context) {
		this.context = context;
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("Now Login......");
		progressDialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... loginInfo) {

		final String username = loginInfo[0];
		final String password = loginInfo[1];
		final String roletype = loginInfo[2];
		final String domain = loginInfo[3];
		ip = loginInfo[4];
		
		String loginResponse = null;
		try {
			loginResponse = CommonAPICalls.login(username, password, roletype, domain, ip, context);
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
		
		postProgress(waitingTime);
		
		return loginResponse;
	}
	
	protected void onProgressUpdate(Integer... progress) {
		progressDialog.setProgress(progress[0]);
		if (progress[0] == 0) progressDialog.show();
		if (progress[0] >= waitingTime) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.severe("Timer has been stopped");
			}
			progressDialog.dismiss();
		}
    }
	
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		/** if login successfully, forward to Main activity temporarily**/
		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		if (status != null && status.equals("200")) {
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "Login");
			toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			new BravoAlertDialog(context).showDialog("Login Failed", msg, "OK");
		}
	}
	
	private void postProgress(int sec) {
		for (int i=0; i <= sec; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.severe("Timer has been stopped");
			}
			publishProgress(i);
		}
	}
}
