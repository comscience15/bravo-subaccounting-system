package com.bravo.bravoclient.common;

import java.util.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bravo.bravoclient.R;

/**
 * This class is the common place to star the scanner
 * @author daniel
 *
 */
public class CommonScannerHandler {

	private static final Logger logger = Logger.getLogger(CommonScannerHandler.class.getName());
	
	/**
	 *  This method is going to start scanner from zxing library
	 */
	public static void startScanner(Context context) {
		/** Getting screen size */
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		/** Referring to zxing barcode scanner library */
		try {
			Intent zxingIntent = new Intent(
					"com.google.zxing.client.android.SCAN");
			// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			zxingIntent.putExtra("SCAN_WIDTH", width);
			zxingIntent.putExtra("SCAN_HEIGHT", height);
			((Activity) context).startActivityForResult(zxingIntent, 0);
		} catch (Exception e) {
			// Barcode scanner exception, we should somehow catch it
			logger.severe("Cannot start scanner: " + e.getMessage());
			Toast.makeText(context, R.string.failure_start_scanner, Toast.LENGTH_LONG).show();
		}
	}
}
