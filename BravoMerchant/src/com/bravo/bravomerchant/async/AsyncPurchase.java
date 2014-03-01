package com.bravo.bravomerchant.async;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.activities.MessageActivity;
import com.bravo.bravomerchant.bean.OrderItem;
import com.bravo.bravomerchant.util.ArithUtil;
import com.bravo.bravomerchant.util.JsonUtil;
import com.bravo.https.apicalls.MerchantAPICalls;
import com.bravo.https.util.BravoAuthenticationException;
import com.bravo.https.util.HttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * 
 * @author jiawl
 *
 */
public class AsyncPurchase extends AsyncTask<String, Void, String>{
	
	private Context context;
	private Double totalPrice = 0d;
	private static final Logger logger = Logger.getLogger(AsyncPurchase.class.getName());
	
	public AsyncPurchase(Context context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... purchaseInfo) {

		final String ip = purchaseInfo[0];
		String orderItemListJsonStr = purchaseInfo[1];
		String cardInfo = purchaseInfo[2];//from qr code
		
		String res = "error";

		try {

			res = MerchantAPICalls.purchaseItems(ip, context, initParams(orderItemListJsonStr, cardInfo));
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
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (BravoAuthenticationException e) {

			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * handle the result from interface
	 */
	@Override
	protected void onPostExecute(String result) {

		String status = HttpResponseHandler.parseJson(result, "status");
		String msg = HttpResponseHandler.parseJson(result, "message");
		
		Intent toShowMsgIntent = new Intent();
		toShowMsgIntent.setClass(context, MessageActivity.class);
		
		if (status != null && !status.equals("404")) {//purchase successfully

			toShowMsgIntent.putExtra("msg", context.getString(R.string.purchase_successful));
		} else {//purchase failed

			toShowMsgIntent.putExtra("msg", context.getString(R.string.purchase_failed) + msg);
		}
		
		context.startActivity(toShowMsgIntent);
	}
	
	/**
	 * get the data for the purchase-interface
	 * @param orderItemListJsonStr 
	 * @param cardInfo from qr code
	 * @return
	 * @throws JSONException
	 */
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
		
		logger.warning("Card info is" + cardInfo);
		res.add(new BasicNameValuePair("encryptedInfo", cardInfo));
		res.add(new BasicNameValuePair("merchantTimestamp", String.valueOf(System.currentTimeMillis())));
		res.add(new BasicNameValuePair("totalAmount", String.valueOf(totalPrice)));
		res.add(new BasicNameValuePair("loyaltyPoints", "0"));
		res.add(new BasicNameValuePair("location", "craig"));
		
		return res;
	}
}
