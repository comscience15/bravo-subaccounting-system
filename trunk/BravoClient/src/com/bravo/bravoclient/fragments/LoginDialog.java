package com.bravo.bravoclient.fragments;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.util.HttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Daniel
 *
 */
public class LoginDialog extends DialogFragment {
	private static EditText usernameView;
	private static EditText passwordView;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder loginDialog = new AlertDialog.Builder(getActivity());

		/** Configure the customized view */
		LayoutInflater inflater = getActivity().getLayoutInflater();
		loginDialog.setView(inflater.inflate(R.layout.dialog_login, null));

		usernameView = (EditText) getActivity().findViewById(R.id.username);
		passwordView = (EditText) getActivity().findViewById(R.id.password);
		
		/** Configure the dialog */
		this.setCancelable(false);
		loginDialog.setPositiveButton("Log In",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								loginValidation();
							}
						}).start();
						
					}
				});
		
		loginDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							/** When cancle clicked, go back to main page**/
							Intent toMainPageIntent = new Intent(getActivity(), MainActivity.class);
							startActivity(toMainPageIntent);
							/** Disable the transaction effect*/
							getActivity().overridePendingTransition(0, 0);
						} catch (Exception e) {
							// go back exception, we should some how catch it
							System.err.println("Can not cancle dialog");
						}
					}
				});

		return loginDialog.create();
	}
	
	
	/**
	 * Generate the login http post request
	 * @param username
	 * @param password
	 */
	private void loginHttpRequest(String username, String password) {
		/**Creating Http Client*/
		HttpClient httpClient = new DefaultHttpClient();
		
		/**Creating login post*/
		final String ip = getString(R.string.IP_Address);
		final String path = "service/authentication/j_spring_security_check";
		final String URL = ip + path;
		HttpPost loginPost = new HttpPost(URL);
		
		/**Creating parameters*/
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("j_username", username));
		nameValuePair.add(new BasicNameValuePair("j_password", password));
		nameValuePair.add(new BasicNameValuePair("j_domain", "200"));
		nameValuePair.add(new BasicNameValuePair("j_roletype", "customer"));
		
		/**URL encoding the post parameters*/
		try {
		    loginPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		}
		catch (UnsupportedEncodingException e) {
		    // writing error to Log
			System.err.println("Encoding Parameter Exception: " + e.toString());
		    //e.printStackTrace();
		}
		
		/**Making HTTP Request*/
		try {
		    HttpResponse response = httpClient.execute(loginPost);
		 
		    HttpResponseHandler responseHandler = new HttpResponseHandler();
		    
		    String loginStatus = responseHandler.parseJson(response, "status");
		    System.out.println("Status: " + loginStatus);
		 
		} catch (ClientProtocolException e) {
		    // writing exception to log
			System.err.println("Client Protocol Exception: " + e.toString());
		    //e.printStackTrace();
		         
		} catch (IOException e) {
		    // writing exception to log
			System.err.println("IO Exception: " + e.toString());
		    //e.printStackTrace();
		}
	}
	
	/**
	 * The login validation
	 * @param username
	 * @param password
	 */
	private void loginValidation() {
		final String username;
		final String password;
		
		if (usernameView != null) {
		    username = usernameView.getText().toString();
		} else {username = "";}
		
		if (passwordView != null) {
			password = passwordView.getText().toString();
		} else {password = "";}
	 
		if (!username.matches("[a-z0-9A-Z.]+@[a-z0-9A-Z.]+[a-z0-9A-Z]+") ) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					/** Setting the toast*/
			    	Context context = getActivity().getApplicationContext();
			    	int duration = Toast.LENGTH_SHORT;
			    	CharSequence text = getString(R.string.invalid_username);
			    	Toast toast = Toast.makeText(context, text, duration);
			    	toast.show();
			    	
				}});
			
		} else if (!password.matches("[a-z0-9A-Z]+") || !(password.length() >=6)){
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					/** Setting the toast*/
			    	Context context = getActivity().getApplicationContext();
			    	int duration = Toast.LENGTH_SHORT;
			    	CharSequence text = getString(R.string.invalid_password);
			    	Toast toast = Toast.makeText(context, text, duration);
			    	toast.show();
				}});
		} else {
			loginHttpRequest(username, password);
		}
		
	}
}
