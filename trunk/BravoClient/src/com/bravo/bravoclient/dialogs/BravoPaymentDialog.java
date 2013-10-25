package com.bravo.bravoclient.dialogs;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.util.BravoAlertDialogInterface;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.encode.QRCodeEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * This class is the dialog implementation for QRcode payment
 * For qrCode layout, can go to layout.do_payment.xml for any changes
 * @author Daniel
 *
 */
public class BravoPaymentDialog implements BravoAlertDialogInterface{
	private Builder alertDialogBuilder;
	private Context context;
	private QRCodeEncoder qrCodeEncoder;
	private View qrCodeView;
	private Bitmap bitmap;
	private AlertDialog dialog;
	
	public BravoPaymentDialog(Context context) {
		this.context = context;
		// Configure builder
		setAlertDialogBuilder();
		// Configure dialog
		setDialog(null, null, null);
	}

	/**
	 * This method should complete all the configuration of alert dialog builder, like cancelable
	 */
	@Override
	public Builder setAlertDialogBuilder() {
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setCancelable(true);
		return alertDialogBuilder;
	}

	/**
	 * Setting title for the dialog
	 */
	@Override
	public void setTitle(String title, Builder alertDialogBuilder) {
		alertDialogBuilder.setTitle(title);
	}

	/**
	 * Setting display message for the dialog
	 */
	@Override
	public void setMessage(String message, Builder alertDialogBuilder) {
		alertDialogBuilder.setMessage(message);
	}

	/**
	 * Setting and doing configurations for the button on dialog
	 */
	@Override
	public void setButton(String name, Builder alertDialogBuilder) {
		alertDialogBuilder.setNegativeButton(name, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO do something when cancel has been clicked
			}
		});
	}
	
	/**
	 * This method should be called before showing the dialog,
	 * since we are going to add bitmap on the top of dialog content
	 * @param bitmap
	 */
	private void setBitMap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	/**
	 * This method is for any configurations of dialog, you can do any configurations inside
	 */
	@Override
	public void setDialog(String title, String msg, String buttonMsg) {
		// Configuring alert dialog
		dialog = alertDialogBuilder.create();
		
		// Apply the customized layout to dialog
		qrCodeView = LayoutInflater.from(context).inflate(R.layout.do_a_payment, null);
		dialog.setView(qrCodeView);

		// Get device window size
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point windowSize = new Point();
		wm.getDefaultDisplay().getSize(windowSize);
		
		// Calculate the suitable size for dialog displaying
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = (int) (windowSize.x * 2/3);
		lp.height = (int) (windowSize.y * 2/3);
		
		// Apply layout configuration to dialog
		dialog.getWindow().setAttributes(lp);
		
		setTitle(title, alertDialogBuilder);
		setMessage(msg, alertDialogBuilder);
		setButton(buttonMsg, alertDialogBuilder);
	}
	
	/**
	 * This method will return the bitmap which has been applied to the dialog
	 * @return
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	/**
	 * This method will return the height of dialog
	 * @return
	 */
	public int getHeight() {
		return dialog == null ? 0 : dialog.getWindow().getAttributes().height;
	}
	
	/**
	 * This method will return the width of dialog
	 * @return
	 */
	public int getWidth() {
		return dialog == null ? 0 : dialog.getWindow().getAttributes().width;
	}
	
	/**
	 * Showing dialog
	 */
	@Override
	public void showDialog() {
		dialog.show();
	}
	
	/**
	 * This method is going to encode the msg and display as QRCode
	 * @param msg The msg which you want to be displayed in QRCode
	 */
	public void generateQRCode(String msg) {
		// Get the dimension for qrcode displaying
		int dialogHeight = getHeight();
		int dialogWidth = getWidth();
		int dimension = dialogHeight < dialogWidth ? dialogHeight : dialogWidth;
				
		// This intent is used for qrcode library
		Intent intent = new Intent();
		intent.setAction(Intents.Encode.ACTION);
		intent.putExtra(Intents.Encode.DATA, msg);
		intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
				
		try {
			qrCodeEncoder = new QRCodeEncoder((Activity) context, intent, dimension, false);
		} catch (Exception e) {
			System.err.println("Failed to construct qrcode encoder");
			e.printStackTrace();
		}
				
		try {
			bitmap = qrCodeEncoder.encodeAsBitmap();
		} catch (WriterException e) {
			System.err.println("Failed to encode as bitmap");
			e.printStackTrace();
		}
				
		// Apply bitmap to dialog
		setBitMap(bitmap);
		
		// Get the image view and add bitmap to it
		ImageView qrcodeImage = (ImageView) qrCodeView.findViewById(R.id.qrcode_view);
		qrcodeImage.setImageBitmap(bitmap);
				
		// Display QRCode dialog
		showDialog();
	}
}
