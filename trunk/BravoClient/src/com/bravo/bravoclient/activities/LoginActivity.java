package com.bravo.bravoclient.activities;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.async.AsyncLogin;
import com.bravo.bravoclient.common.CommonViewHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
/**
 * This class is for log in activity, and login info validation
 * The network request part is in AsyncLogin.java
 * @author Daniel
 * @email danniel1205@gmail.com
 *
 */
public class LoginActivity extends Activity {
	/** Declaring buttons**/
	private static Button loginBtn;
	private static Button registerBtn;
	
	/** Declaring text fields**/
	private static EditText usernameField;
	private static EditText passwordField;
	
	private static String username;
	private static String password;
	private static String ip;
	private static final String roleType = "customer";
	private static final String domain = "200";
	
	private boolean isValidP = false;
	private boolean isValidU = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // Stop keyboard automatically generated
		setContentView(R.layout.activity_login);
		
		ip = getString(R.string.IP_Address);
		
		/** Declaring buttons**/
		loginBtn = (Button) findViewById(R.id.login_loginBtn);
		loginBtn.setEnabled(false);
		registerBtn = (Button) findViewById(R.id.login_registerBtn);
		
		/** Declaring text fields**/
		usernameField = (EditText) findViewById(R.id.login_email);
		passwordField = (EditText) findViewById(R.id.login_password);
		
		/** Declaring the username watcher**/
		TextWatcher usernameWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String usernameTemp = CommonViewHandler.getEditTextValue(usernameField);
				if (!usernameValidation(usernameTemp)) {
					loginBtn.setEnabled(false);
					isValidU = false;
					String errorInfo = getString(R.string.invalid_username);
					usernameField.setError(errorInfo);
				} else {
					isValidU = true;
					if (isValidU && isValidP) {
						loginBtn.setEnabled(true);
					}
					username = usernameTemp;
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
		};
		
		/** Declaring the password watcher**/
		TextWatcher passwordWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String passwordTemp = CommonViewHandler.getEditTextValue(passwordField);
				if (!passwordValidation(passwordTemp)) {
					loginBtn.setEnabled(false);
					isValidP = false;
					String errorInfo = getString(R.string.invalid_password);
					passwordField.setError(errorInfo);
				} else {
					isValidP = true;
					if (isValidU && isValidP) {
						loginBtn.setEnabled(true);
					}
					password = passwordTemp;
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
		};
		
		/** Adding the watcher to fields**/
		usernameField.addTextChangedListener(usernameWatcher);
		passwordField.addTextChangedListener(passwordWatcher);	
		
		
		
		/** Setting up the click listener for login button**/
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/** When login button is pressed, it will go to asynchronized task for process **/
				new AsyncLogin(LoginActivity.this).execute(username, password, roleType, domain ,ip);
				
				// Close soft keyboard after button is clicked
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
			}
		});
		
		/** Setting up the click listener for Register button**/
		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toRegisterActivity = new Intent(LoginActivity.this.getApplicationContext(), RegisterActivity.class);
				LoginActivity.this.startActivity(toRegisterActivity);
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		Intent toLoginActivity = new Intent(LoginActivity.this.getApplicationContext(), MainActivity.class);
    	LoginActivity.this.startActivity(toLoginActivity);
    	LoginActivity.this.overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
	}
	
	/**
	 * Get the username, this is aim to avoid null point exception
	 * when user types nothing
	 * @param usernameView
	 * @return 
	 */
	private String getUsername(EditText usernameView) {
		return usernameView == null ? "" : usernameView.getText().toString(); 
	}
	
	/**
	 * Get the password, this is aim to avoid null point exception
	 * when user types nothing
	 * @param passwordView
	 * @return
	 */
	private String getPassword(EditText passwordView) {
		return passwordView == null ? "" : passwordView.getText().toString();
	}
	
	/**
	 * The username validation
	 * @param username
	 * @param password
	 */
	private boolean usernameValidation(String username) {
		/** The username right now is not email address **/
//		if (!username.matches("[a-z0-9A-Z.]+@[a-z0-9A-Z.]+[a-z0-9A-Z]+") ) {
//			return false;
//		} else {
//			return true;
//		}
//		if (!username.matches("[a-z0-9A-Z.]+")) {
//			return false;
//		} else return true;
		return true;
	}
	
	/**
	 * The password validation
	 * @param password
	 * @return
	 */
	private boolean passwordValidation(String password) {
		if (!password.matches("[a-z0-9A-Z]+") || !(password.length() >=4)){
			return false;
		} else {
			return true;
		}
	}
}
