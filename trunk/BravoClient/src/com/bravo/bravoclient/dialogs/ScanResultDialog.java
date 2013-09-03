package com.bravo.bravoclient.dialogs;

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
public class ScanResultDialog extends DialogFragment {
	private AlertDialog.Builder resultHandlerDialog;
	private String positiveButtonMessage = "OK";
	private String negativeButtonMessage = "Cancel";
	private String scanResult;
	private String scanFormat;
	
	
	public interface ScanResultListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	ScanResultListener ScanResultListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            ScanResultListener = (ScanResultListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement HomeScanResultListener");
        }
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		resultHandlerDialog = new AlertDialog.Builder(getActivity());
		//resultHandlerDialog.setCancelable(false);
		resultHandlerDialog.setTitle("Barcode Scanner")
			.setMessage("The result of scanning: \n" + scanResult)
			.setPositiveButton(positiveButtonMessage, new DialogInterface.OnClickListener() {
				/** Positive listener*/
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ScanResultListener.onDialogPositiveClick(ScanResultDialog.this);
				}
			})
			.setNegativeButton(negativeButtonMessage, new DialogInterface.OnClickListener() {
				/** Negative listener*/
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ScanResultListener.onDialogNegativeClick(ScanResultDialog.this);
				}
			});

		return resultHandlerDialog.create();
	}
	
	/** Configuration of dialog*/
	public void setPositiveButtonMsg(String positiveButtonMsg) {
		positiveButtonMessage = positiveButtonMsg;
	}
	
	public void setNegativeButtonMsg(String negativeButtonMsg) {
		negativeButtonMessage = negativeButtonMsg;
	}
	
	public void setScanResult(String scanResult) {
		this.scanResult = scanResult;
	}
	
	public void setScanFormat(String format) {
		this.scanFormat = format;
	}
	
}
