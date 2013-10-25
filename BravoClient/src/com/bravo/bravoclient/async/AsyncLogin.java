package com.bravo.bravoclient.async;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.util.BravoAlertDialogInterface;
import com.bravo.bravoclient.util.HttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
		return loginHttpRequest(loginInfo[0], loginInfo[1], loginInfo[2]);
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		System.err.println("Result code is: " + result);
		/** if login successfully, forward to Main activity temporarily**/
		if (result != null && !result.equals("404")) {
			Intent toCardsFragment = new Intent(context, MainActivity.class);
			toCardsFragment.putExtra("Activity", "Login");
	    	context.startActivity(toCardsFragment);
	    	((Activity) context).overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
		} else {
			/** if login unsuccessfully, showing the alert dialog**/
			System.out.println("Debugging for showing login dialog"); // this should be removed
			new BravoAlertDialog(context).showDialog("Login Failed", "Please check your authentication or network connection", "OK");
		}
	}
	
	/**
	 * Generate the login http post request
	 * @param username
	 * @param password
	 */
	private String loginHttpRequest(String username, String password, String IP) {
		/**Creating Http Client*/
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		/**Creating login post*/
		final String ip = IP; //context.getString(R.string.IP_Address);
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
			return null;
		}
		
		/**Making HTTP Request*/
		try {
		    HttpResponse response = httpClient.execute(loginPost);
		    
		    List<Cookie> cookies = httpClient.getCookieStore().getCookies();
		    for (Cookie c : cookies) {
		    	System.err.println("Cookie is:\n Comment:" + c.getComment() +"\n CommentURL:" + c.getCommentURL() 
		    			+"\n Domain:" + c.getDomain() +"\n Name:" + c.getName() +"\n Path:" + c.getPath() +"\n Value:" + c.getValue()
		    			+"\n Version:" + c.getVersion() +"\n");
		    }
		    
		    HttpResponseHandler responseHandler = new HttpResponseHandler();
		    
		    String loginStatus = responseHandler.parseJson(response, "status");
		    System.out.println("Status: " + loginStatus); // This should be deleted after release
		    // loginStatus is null means login successfully, see API document for details
		    return loginStatus == null ? "200" : loginStatus;
		 
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
