package com.bravo.bravoclient.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import com.bravo.bravoclient.util.BravoAlertDialogInterface;

/**
 * This class is general implementation for alert dialog which is
 * implemented BravoAlertDialogInterface
 * @author Daniel
 * @email danniel1205@gmail.com
 *
 */
public class BravoAlertDialog implements BravoAlertDialogInterface{
	private Context context;
	private Builder alertDialogBuilder;
	
	public BravoAlertDialog(Context context) {
		this.context = context;
	}
	
	/**
	 * The method is going to show alert dialog
	 */
	public void alertDialog(String title, String msg) {
		setAlertDialogBuilder();
		setTitle(title, alertDialogBuilder);
		setMessage(msg, alertDialogBuilder);
		setButton("OK", alertDialogBuilder);
		showDialog(alertDialogBuilder);
	}
	
	/**
	 *  This method should complete all the configuration of alert dialog builder, like cancelable
	 */
	@Override
	public Builder setAlertDialogBuilder() {
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setCancelable(true);
		return alertDialogBuilder;
	}

	@Override
	public void setTitle(String title, Builder alertDialogBuilder) {
		alertDialogBuilder.setTitle(title);
	}

	@Override
	public void setMessage(String message, Builder alertDialogBuilder) {
		alertDialogBuilder.setMessage(message);
	}

	@Override
	public void setButton(String name, Builder alertDialogBuilder) {
		alertDialogBuilder.setNegativeButton(name, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}

	@Override
	public void showDialog(Builder alertDialogBuilder) {
		alertDialogBuilder.create().show();
	}
}
