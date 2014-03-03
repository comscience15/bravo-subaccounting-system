package com.bravo.bravoclient.dialogs;

import java.util.logging.Logger;

import android.app.ProgressDialog;
import android.content.Context;

public class BravoProgressDialog {
	private static final Logger logger = Logger.getLogger(BravoProgressDialog.class.getName());
	
	public static void showLoadingDialog(Context context, final int sec) {
		ProgressDialog loadingDialog = new ProgressDialog(context);
		loadingDialog.setCancelable(false);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setMessage("Loading...");
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		loadingDialog.setProgress(0);
		loadingDialog.setMax(100);
		loadingDialog.show();
		 
		for (int i = 0; i< sec; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.warning(e.getMessage());
			}
		}
		
		loadingDialog.dismiss();
	}

}
