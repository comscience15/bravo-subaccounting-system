package com.bravo.bravomerchant.activities;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.async.AsyncPurchase;
import com.bravo.bravomerchant.dialogs.ScanResultDialog;
import com.bravo.bravomerchant.dialogs.ScanResultDialog.ScanResultListener;
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
 * @author Daniel Guo
 * 
 */
public class ScannerActivity extends FragmentActivity implements
		ScanResultListener {
	private static String scanResult;
	private static String productsCode = "";
	private static String orderItemListJsonStr;
	
	private static final Logger logger = Logger.getLogger(ScannerActivity.class.getName());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Removing title bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_scanner);

		Intent sourceIntent = getIntent();
		if(sourceIntent.hasExtra("orderItemListJsonStr")){
			
			orderItemListJsonStr = sourceIntent.getStringExtra("orderItemListJsonStr");
		}
		if(sourceIntent.hasExtra("productsCode")){
			
			productsCode = sourceIntent.getStringExtra("productsCode");
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

				scanResult = getScanRes(intent);
				
				if (orderItemListJsonStr != null && !orderItemListJsonStr.equals("")){
					new AsyncPurchase(ScannerActivity.this).execute(getString(R.string.IP_Address), orderItemListJsonStr, scanResult);
				} else {
					productsCode += scanResult + ",";
				}
				startScanner();
			} else if (resultCode == RESULT_CANCELED) {
				
				if(productsCode.equals("")){//when no products, back to mainActivity
				
					Intent backToMain = new Intent();
					backToMain.setClass(this, MainActivity.class);
					startActivity(backToMain);
					ScannerActivity.this.finish();
				}else{//ready to get the cardInfo

					Intent toOrderConfirmIntent = new Intent();
					toOrderConfirmIntent.setClass(this, OrderConfirmActivity.class);
					toOrderConfirmIntent.putExtra("productsCode", productsCode);
					startActivity(toOrderConfirmIntent);
					ScannerActivity.this.finish();
				}
			}
		}
	}
	
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
			// Barcode scanner exception, we should somehow catch it
			System.err.println("Can not start barcode scanner: " + e.toString());
		}
	}
	
	/**
	 *  This method is going to open the popup dialog
	 */
	public void showDialog(String msg) {
		try {
			ScanResultDialog dialog = new ScanResultDialog();
			dialog.setCancelable(false); // After the setting, the dialog will not be closed by touching other places
//			dialog.setPositiveButtonMsg(getString(R.string.home_scanner_positive_button));
			dialog.setNegativeButtonMsg(getString(R.string.home_scanner_finish_button));
			dialog.setScanResult(msg);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(dialog, null);
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			System.err.println("Can not open dialog: \n" + e.toString());
		}
	}

	/**
	 *  This method is overridden from HomeScanResultDialog, 
	 *  in order to handle the positive button clicked 
	 */
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		try {
			Intent SearchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
			SearchIntent.putExtra(SearchManager.QUERY, scanResult);
			SearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(SearchIntent);
			
			/** Finish the current activity, in order to go back to home screen when
			 * back button clicked
			 */
			finish();
		} catch (Exception e) {
			// google search exception, we should some how catch it
			System.err.println("Can not go to search: " + e.toString());
		}
	}

	/**
	 *  This method is overridden from HomeScanResultDialog, 
	 *  in order to handle the negative button clicked 
	 */
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		try {
			Intent toMainPageIntent = new Intent(this, MainActivity.class);
			startActivity(toMainPageIntent);
		} catch (Exception e) {
			System.err.println("Can not cancle the dialog: " + e.toString());
		}
	}
}
