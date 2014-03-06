package com.bravo.bravoclient.activities;

import java.text.MessageFormat;
import java.util.logging.Logger;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.R.layout;
import com.bravo.bravoclient.common.CommonScannerHandler;
import com.bravo.bravoclient.common.CommonValidationHandler;
import com.bravo.bravoclient.common.CommonViewHandler;
import com.bravo.bravoclient.dialogs.ScanResultDialog.ScanResultListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SendMoneyActivity extends Activity{
	
	private static Button emailConfirmButton;
	private static Button scannerButton;
	private static EditText amountField;
	private static EditText emailField;
	private static EditText memoField;
	
	private static String amount;
	private static String email;
	private static String memo;
	
	private boolean isValidEmail;
	private boolean isValidAmount;
	
	private static String scanResult;
	private static String format;
	
	private static final Logger logger = Logger.getLogger(SendMoneyActivity.class.getName());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // Stop keyboard automatically generated
		setContentView(R.layout.activity_send_money);
		
		emailConfirmButton = (Button) findViewById(R.id.send_money_email_confirm_button);
		scannerButton = (Button) findViewById(R.id.send_money_scanner_button);
		
		amountField = (EditText) findViewById(R.id.send_money_amount);
		emailField = (EditText) findViewById(R.id.send_money_email);
		memoField = (EditText) findViewById(R.id.send_money_memo);
		setWatcher();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				scanResult = intent.getStringExtra("SCAN_RESULT");
				format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				/** Starting the dialog*/
				displayConfirmDialog();
			} else if (resultCode == RESULT_CANCELED) {
				
			}
		}
	}
	
	private void setWatcher() {
		emailConfirmButton.setEnabled(false);
		/** email watcher **/
		TextWatcher emailWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String emailTemp = CommonViewHandler.getEditTextValue(emailField);
				if (emailValidation(emailTemp)) {
					isValidEmail = true;
					emailField.setError(null);
					if (allFieldsValidated()) emailConfirmButton.setEnabled(true);
					email = emailTemp;
				} else {
					isValidEmail = false;
					emailConfirmButton.setEnabled(false);
					emailField.setError(getString(R.string.send_money_invalid_email));
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}};
			
			/** Amount watcher **/
			TextWatcher amountWatcher = new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
					final String amountTemp = CommonViewHandler.getEditTextValue(amountField);
					if (amountValidation(amountTemp)) {
						isValidAmount = true;
						amountField.setError(null);
						if (allFieldsValidated()) emailConfirmButton.setEnabled(true);
						amount = amountTemp;
					} else {
						isValidAmount = false;
						emailConfirmButton.setEnabled(false);
						amountField.setError(getString(R.string.send_money_invalid_amount));
					}
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
				}};
			
			// Scanner button listener
			scannerButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					displayAlertDialog();
				}
			});
			
			// Using email to send money listener
			emailConfirmButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					displayConfirmDialog();
				}
			});
				
			emailField.addTextChangedListener(emailWatcher);
			amountField.addTextChangedListener(amountWatcher);
	}
	
	private void displayAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.alert_send_money_confirm_scanner);
		builder.setCancelable(false);
		builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO: Go to scanner
				scanResult = ""; // reset the scan result before start scanner
				CommonScannerHandler.startScanner(SendMoneyActivity.this);
			}
		});
		builder.create().show();
	}
	
	private void displayConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.alert_send_money_confirm_info_title);
		String msg = "Send money to: {0} \nAmount: {1}{2} \nMemo: {3}";
		if (scanResult != null && scanResult.isEmpty() == false) {
			msg = MessageFormat.format(msg, "QRCode provider", getString(R.string.currency), amount, CommonViewHandler.getEditTextValue(memoField));
		} else if (email != null && email.isEmpty() == false){
			msg = MessageFormat.format(msg, email, amount, getString(R.string.currency), CommonViewHandler.getEditTextValue(memoField));
		}
		builder.setMessage(msg);
		builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	
	private boolean emailValidation(String email) {
		return CommonValidationHandler.usernameValidation(email);
	}
	
	private boolean amountValidation(String amount) {
		return CommonValidationHandler.amountValidation(amount);
	}
	
	private boolean allFieldsValidated() {
		return isValidEmail && isValidAmount;
	}
}
