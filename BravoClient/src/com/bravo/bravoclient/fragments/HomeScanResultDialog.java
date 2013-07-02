package com.bravo.bravoclient.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Daniel
 *
 */
public class HomeScanResultDialog extends DialogFragment {
	private String scanResult;
	private String scanFormat;
	
	public interface HomeScanResultListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
		//public void onDialogNeutralClick(DialogFragment dialog);
	}
	
	HomeScanResultListener homeScanResultListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            homeScanResultListener = (HomeScanResultListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement HomeScanResultListener");
        }
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder resultHandlerDialog = new AlertDialog.Builder(getActivity());
		resultHandlerDialog.setTitle("Barcode Scanner")
			.setMessage("The result of scanning: \n" + scanResult)
			.setPositiveButton("Go Searching", new DialogInterface.OnClickListener() {
				/** Positive listener*/
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					homeScanResultListener.onDialogPositiveClick(HomeScanResultDialog.this);
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				/** Negative listener*/
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					homeScanResultListener.onDialogNegativeClick(HomeScanResultDialog.this);
				}
			});
//			.setNeutralButton("Display Result", new DialogInterface.OnClickListener() {
//				/** Neutral listener*/
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					homeScanResultListener.onDialogNeutralClick(HomeScanResultDialog.this);
//				}
//			});
		return resultHandlerDialog.create();
	}
	
	public void setScanResult(String scanResult) {
		this.scanResult = scanResult;
	}
	
	public void setScanFormat(String format) {
		this.scanFormat = format;
	}
	
}
