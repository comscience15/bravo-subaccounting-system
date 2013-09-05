package com.bravo.bravoclient.util;

import android.app.AlertDialog;

/**
 * This is the interface of alter dialog
 * @author Daniel
 * @email danniel1205@gmail.com
 *
 */
public interface BravoAlertDialogInterface {
	/**
	 * This method should complete all the configuration of alert dialog builder, like cancelable
	 * @return
	 */
	public AlertDialog.Builder setAlertDialogBuilder();
	/**
	 * Setting the title for dialog
	 * @param title
	 * @param alertDialogBuilder
	 */
	public void setTitle(String title, AlertDialog.Builder alertDialogBuilder);
	/**
	 * Setting the message to display
	 * @param message
	 * @param alertDialogBuilder
	 */
	public void setMessage(String message, AlertDialog.Builder alertDialogBuilder);
	/**
	 * Setting the button action which should also be added an listener
	 * @param name
	 * @param alertDialogBuilder
	 */
	public void setButton(String name, AlertDialog.Builder alertDialogBuilder);
	/**
	 * Showing Dialog
	 * @param alertDialogBuilder
	 */
	public void showDialog(AlertDialog.Builder alertDialogBuilder);
}
