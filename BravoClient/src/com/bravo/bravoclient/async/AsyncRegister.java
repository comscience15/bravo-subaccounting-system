package com.bravo.bravoclient.async;

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
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.util.APICallsFactory;
import com.bravo.bravoclient.util.HttpResponseHandler;

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
	public AsyncRegister(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... registerInfo) {
		return APICallsFactory.register(registerInfo[0], registerInfo[1], registerInfo[2], registerInfo[3], registerInfo[4], registerInfo[5], registerInfo[6]);
		//return  registerHttpRequest(registerInfo[0], registerInfo[1], registerInfo[2], registerInfo[3], registerInfo[4], registerInfo[5], registerInfo[6]);
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		System.err.println(result);
		/** if register successfully, forward to Main activity temporarily**/
		if (result != null && !result.equals("404")) {
			Intent toLoginActivity = new Intent(context, MainActivity.class);
	    	context.startActivity(toLoginActivity);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			/** if register unsuccessfully, showing the alert dialog**/
			new BravoAlertDialog(context).showDialog("Register Failed", "Please check your authentication or network connection", "OK");
		}
	}
	
	private String registerHttpRequest(String username, String password, String street, String city, String state, String zipCode, String IP) {
		/**Creating Http Client*/
		HttpClient httpClient = new DefaultHttpClient();
		
		/**Creating login post*/
		final String ip = IP; //context.getString(R.string.IP_Address);
		final String path = "service/authentication/signup";
		final String URL = ip + path;
		HttpPost loginPost = new HttpPost(URL);
		
		/**Creating parameters*/
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("j_username", username));
		nameValuePair.add(new BasicNameValuePair("j_password", password));
		nameValuePair.add(new BasicNameValuePair("street", street));
		nameValuePair.add(new BasicNameValuePair("city", city));
		nameValuePair.add(new BasicNameValuePair("state", state));
		nameValuePair.add(new BasicNameValuePair("zip", zipCode)); // Should change the back end to "zipCode"
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
			return null;
		}
		
		/**Making HTTP Request*/
		try {
		    HttpResponse response = httpClient.execute(loginPost);
		 
		    HttpResponseHandler responseHandler = new HttpResponseHandler();
		    
		    String registerStatus = responseHandler.parseJson(response, "status");
		    System.out.println("Status: " + registerStatus);
		    // loginStatus is null means login successfully, see API document for details
		    return registerStatus == null ? "200" : registerStatus;
		 
		} catch (ClientProtocolException e) {
		    // writing exception to log
			System.err.println("Client Protocol Exception: " + e.toString());
		    //e.printStackTrace();
			return null;
		         
		} catch (IOException e) {
		    // writing exception to log
			System.err.println("IO Exception: " + e.toString());
		    //e.printStackTrace();
			return null;
		}
	}
}
