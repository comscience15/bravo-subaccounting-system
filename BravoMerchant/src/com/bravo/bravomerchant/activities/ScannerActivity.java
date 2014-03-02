package com.bravo.bravomerchant.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;
import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.async.AsyncPurchase;
import com.bravo.bravomerchant.util.NFCHandler;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * This class is the activity for barcode scanning feature on home tab.
 * It implements the listener interface from HomeScanResultDialog.java.
 * And calls the zxing barcode scanner project lib to achieve scanning feature
 * 
 * @author jiawl
 * 
 */
public class ScannerActivity extends FragmentActivity{
	
	private static final Logger logger = Logger.getLogger(ScannerActivity.class.getName());
	
	private static String scanResult;
	private static ArrayList<String> productBarCodesList;
	private static String orderItemListJsonStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Removing title bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scanner);

		Intent sourceIntent = getIntent();
		if(sourceIntent.hasExtra("orderItemListJsonStr")){//parse the orderItem info
			
			orderItemListJsonStr = sourceIntent.getStringExtra("orderItemListJsonStr");
		}
		if(sourceIntent.hasExtra("productBarCodesList")){//parse the productBarCode info
			
			productBarCodesList = sourceIntent.getStringArrayListExtra("productBarCodesList");
		}else{
			
			productBarCodesList = new ArrayList<String>();
		}
        
		startScanner();
	}
	
	@Override
	public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
        	String nfcCardInfo = NFCHandler.readFromDevice(getIntent());
        	
        	logger.info("Get the NFC data from the other device: " + NFCHandler.readFromDevice(getIntent()));
        	logger.info("Order item list is: " + orderItemListJsonStr);

        	if (orderItemListJsonStr != null && !orderItemListJsonStr.equals("")) {
        		new AsyncPurchase(ScannerActivity.this).execute(getString(R.string.IP_Address), orderItemListJsonStr, nfcCardInfo);
        	}
        }
    }
	/**
	 * This method is to handle the result from scanner
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if (requestCode == 0) {
			
			if (resultCode == RESULT_OK) {

				scanResult = getScanRes(intent);//get the scan result
				
				if(orderItemListJsonStr != null
						&& !"".equals(orderItemListJsonStr)){
					//when orderItemListJsonStr is not empty, means the current scanResult is the qr code of the customer and should start the purchase 
					
					new AsyncPurchase(ScannerActivity.this).execute(getString(R.string.IP_Address), orderItemListJsonStr, scanResult);
				}else{
					//when others, the current scanResult is a pruductBarCode
					
					productBarCodesList.add(scanResult);
				}
				startScanner();
			} else if (resultCode == RESULT_CANCELED) {
				
				if(productBarCodesList.isEmpty()){//when no products, back to mainActivity
				
					Intent backToMain = new Intent();
					backToMain.setClass(this, MainActivity.class);
					startActivity(backToMain);
				}else{//turn to the order confirm activity

					Intent toOrderConfirmIntent = new Intent();
					toOrderConfirmIntent.setClass(this, OrderConfirmActivity.class);
					toOrderConfirmIntent.putStringArrayListExtra("productBarCodesList", productBarCodesList);
					startActivity(toOrderConfirmIntent);
				}
				ScannerActivity.this.finish();
			}
		}
	}
	
	/**
	 * get the scan result
	 * @param intent
	 * @return
	 */
	private String getScanRes(Intent intent){
		
		String res = null;

		Iterator<String> keys = intent.getExtras().keySet().iterator();
		String key = "";
		while(keys.hasNext()){
			
			key = keys.next();
			if(key.indexOf("result") >= 0
					|| key.indexOf("RESULT") >= 0){
				
				res = intent.getStringExtra(key) ;
				break;
			}
		}
		
		return res;
	}
	/**
	 *  This method is going to start scanner from zxing library
	 */
	public void startScanner() {
		
		/** Getting screen size */
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		/** Referring to zxing barcode scanner library */
		try {
			Intent zxingIntent = new Intent("com.google.zxing.client.android.SCAN");
			if(orderItemListJsonStr != null
					&& !"".equals(orderItemListJsonStr)){
				//zxingIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			}
			zxingIntent.putExtra("SCAN_WIDTH", width);
			zxingIntent.putExtra("SCAN_HEIGHT", height);
			startActivityForResult(zxingIntent, 0);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Can not start barcode scanner: " + e.toString());
		}
	}
}
