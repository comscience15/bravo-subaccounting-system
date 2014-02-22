package com.bravo.bravomerchant.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.bravomerchant.activities.MainActivity;
import com.bravo.bravomerchant.activities.MessageActivity;
import com.bravo.bravomerchant.activities.OrderConfirmActivity;
import com.bravo.bravomerchant.activities.ScannerActivity;
import com.bravo.bravomerchant.bean.OrderItem;
import com.bravo.bravomerchant.util.ArithUtil;
import com.bravo.bravomerchant.util.JsonUtil;
import com.bravo.https.apicalls.MerchantAPICalls;
import com.bravo.https.util.BravoAuthenticationException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class AsyncPurchase extends AsyncTask<String, Void, String>{
	
	private Context context;
	private Double totalPrice = 0d;
	
	public AsyncPurchase(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... purchaseInfo) {

		final String ip = purchaseInfo[0];
		String orderItemListJsonStr = purchaseInfo[1];
		String cardInfo = purchaseInfo[2];
		
		String res = "error";

		try {
//			MerchantAPICalls.purchaseItems(getString(R.string.IP_Address), getApplicationContext(), initParams());
			res = MerchantAPICalls.purchaseItems(ip, context, initParams(orderItemListJsonStr, cardInfo));
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
		
		return res;
	}
	
	/**
	 * @param The parameter is from doInBackground()
	 */
	@Override
	protected void onPostExecute(String result) {
		
		JSONObject resJsonObj = null;
		String status = null;
		String msg = "";
		
		try {
			resJsonObj = new JSONObject(result);
			status = resJsonObj.getString("status");
			msg = resJsonObj.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Intent toMainActivity = new Intent(context, MainActivity.class);
    	context.startActivity(toMainActivity);
    	

		Intent toShowMsgIntent = new Intent();
		toShowMsgIntent.setClass(context, MessageActivity.class);
		toShowMsgIntent.putExtra("msg", result);
		context.startActivity(toShowMsgIntent);
//		if(!"200".equals(status)){
//			
//		}else{//totalPrice
//			
//		}
	}
	
    private List<NameValuePair> initParams(String orderItemListJsonStr, String cardInfo) throws JSONException {
		
		List<NameValuePair> res = new ArrayList<NameValuePair>();
		List<OrderItem> orderItemList = JsonUtil.parseOrderItemListJsonStr(orderItemListJsonStr);
		OrderItem orderItem = null;
		
		for (int i = 0; i < orderItemList.size(); i++) {
			
			orderItem = orderItemList.get(i);

			res.add(new BasicNameValuePair("orderItemList["+i+"].tax", String.valueOf(orderItem.getTax())));
			res.add(new BasicNameValuePair("orderItemList["+i+"].productID", orderItem.getBarCode()));
			res.add(new BasicNameValuePair("orderItemList["+i+"].productName", orderItem.getName()));
			res.add(new BasicNameValuePair("orderItemList["+i+"].totalPrice", String.valueOf(orderItem.getTotalPrice())));
			res.add(new BasicNameValuePair("orderItemList["+i+"].unit", String.valueOf(orderItem.getUnit())));
			totalPrice = ArithUtil.add(totalPrice, orderItem.getTotalPrice());
		}
		
		res.add(new BasicNameValuePair("encryptedInfo", cardInfo));
		res.add(new BasicNameValuePair("merchantTimestamp", String.valueOf(System.currentTimeMillis())));
		res.add(new BasicNameValuePair("totalAmount", String.valueOf(totalPrice)));
		res.add(new BasicNameValuePair("loyaltyPoints", "0"));
		res.add(new BasicNameValuePair("location", "craig"));
		
		return res;
	}
}
