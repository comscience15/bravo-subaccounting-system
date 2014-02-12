package com.bravo.bravomerchant.async;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.activities.MainActivity;
import com.bravo.bravomerchant.dialogs.BravoAlertDialog;
import com.bravo.bravomerchant.util.HttpResponseHandler;
import com.bravo.https.apicalls.CommonAPICalls;
import com.bravo.https.apicalls.MerchantAPICalls;
import com.bravo.https.util.BravoAuthenticationException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class AsyncPurchase extends AsyncTask<String, Void, String>{
	private Context context;
	public AsyncPurchase(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... purchaseInfo) {

		final String ip = purchaseInfo[0];

		try {
//			MerchantAPICalls.purchaseItems(getString(R.string.IP_Address), getApplicationContext(), initParams());
			MerchantAPICalls.purchaseItems(ip, context, initParams());
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BravoAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		
		System.err.println("Result code is: " + result);
	}
	
    private static List<NameValuePair> initParams() {
		
		List<NameValuePair> res = new ArrayList<NameValuePair>();
		
		for (int i = 0; i < 5; i++) {

			res.add(new BasicNameValuePair("orderItemList["+i+"].tax", "0"));
			res.add(new BasicNameValuePair("orderItemList["+i+"].productID", "1000000"+i));
			res.add(new BasicNameValuePair("orderItemList["+i+"].productName", "productName-"+i));
			res.add(new BasicNameValuePair("orderItemList["+i+"].totalPrice", "20"));
			res.add(new BasicNameValuePair("orderItemList["+i+"].unit", "1"));
		}
		
		res.add(new BasicNameValuePair("encryptedInfo", "�9��ڎ�(��OT[l4��x]��ㆣ�R1��AɄ��q揍UǢi�ww':�R��^���#�E�"));
		res.add(new BasicNameValuePair("merchantTimestamp", String.valueOf((new Date()).getTime())));
		res.add(new BasicNameValuePair("totalAmount", "100"));
		res.add(new BasicNameValuePair("loyaltyPoints", "0"));
		res.add(new BasicNameValuePair("location", "craig"));
		
		return res;
	}
}
