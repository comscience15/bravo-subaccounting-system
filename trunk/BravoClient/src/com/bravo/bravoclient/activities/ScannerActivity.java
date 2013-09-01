package com.bravo.bravoclient.activities;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.dialogs.ScanResultDialog;
import com.bravo.bravoclient.dialogs.ScanResultDialog.ScanResultListener;

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
	private String scanResult;
	private String format;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Removing title bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_scanner);
		
		startScanner();
	}
	
	/**
	 * This method is to handle the result from scanner
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				scanResult = intent.getStringExtra("SCAN_RESULT");
				format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				/** Starting the dialog*/
				showDialog();
			} else if (resultCode == RESULT_CANCELED) {
				/** If scanning failed, will intent to main screen*/
				try {
					Intent toMainPageIntent = new Intent(this, MainActivity.class);
					startActivity(toMainPageIntent);
				} catch (Exception e) {
					// go to main page exception, we should some how catch it
					System.err.println("Can not catch the scanning result: " + e.toString());
				}
			}
		}
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
			Intent zxingIntent = new Intent(
					"com.google.zxing.client.android.SCAN");
			// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
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
	public void showDialog() {
		try {
			ScanResultDialog dialog = new ScanResultDialog();
			dialog.setPositiveButtonMsg(getString(R.string.home_scanner_positive_button));
			dialog.setNegativeButtonMsg(getString(R.string.home_scanner_negative_button));
			dialog.setScanResult(scanResult);
			// dialog.show(getSupportFragmentManager(), "homeScanResultDialog");
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
			// go back exception, we should some how catch it
			System.err.println("Can not cancle the dialog: " + e.toString());
		}
	}
}
