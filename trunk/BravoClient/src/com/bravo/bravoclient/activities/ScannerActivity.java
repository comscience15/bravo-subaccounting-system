package com.bravo.bravoclient.activities;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.R.layout;
import com.bravo.bravoclient.R.menu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * @author Daniel
 *
 */
public class ScannerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Removing title bar */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_scanner);

		/** Getting screen size */
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		/** Referring to zxing barcode scanner library */
		try {
			Intent zxingIntent = new Intent("com.google.zxing.client.android.SCAN");
			// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			zxingIntent.putExtra("SCAN_WIDTH", width);
			zxingIntent.putExtra("SCAN_HEIGHT", height);
			startActivityForResult(zxingIntent, 0);
		} catch (Exception e) {
			// Barcode scanner exception, we should somehow catch it
		}
	}

	/**
	 * This method is to handle the result from scanner
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String scanResult = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				System.out.println("Scanning successfully " + scanResult);
				try {
					Intent SearchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
					SearchIntent.putExtra(SearchManager.QUERY, scanResult);
					startActivity(SearchIntent);
				} catch (Exception e) {
					// google search exception, we should some how catch it
				}
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				System.out.println("Scanning failed");
			}
		}
	}
}
