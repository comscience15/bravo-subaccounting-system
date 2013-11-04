package com.bravo.bravomerchant.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import com.bravo.bravomerchant.util.BravoAlertDialogInterface;

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
		setAlertDialogBuilder();
	}
	
	/**
	 *  This method should complete all the configuration of alert dialog builder, like cancelable
	 */
	@Override
	public Builder setAlertDialogBuilder() {
		System.out.println("SetAlertDialogBuilder is called"); // should be removed
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
	public void showDialog() {
		System.out.println("showDialog() is called"); // should be removed
		alertDialogBuilder.create().show();
	}
	
	/**
	 * This should be used to display the dialog instead of showDialog()
	 * @param title
	 * @param msg
	 * @param buttonMsg
	 */
	public void showDialog(String title, String msg, String buttonMsg) {
		setDialog(title, msg, buttonMsg);
		showDialog();
	}

	@Override
	public void setDialog(String title, String msg, String buttonMsg) {
		System.out.println("setDialog is called"); // should be removed
		setTitle(title, alertDialogBuilder);
		setMessage(msg, alertDialogBuilder);
		setButton(buttonMsg, alertDialogBuilder);
	}
}
