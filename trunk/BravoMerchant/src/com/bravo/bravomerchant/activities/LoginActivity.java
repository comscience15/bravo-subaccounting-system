package com.bravo.bravomerchant.activities;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.async.AsyncLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 * @author wenlong_jia
 * @author Daniel
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
				final String usernameTemp = getEditTextValue(usernameField);
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
				final String passwordTemp = getEditTextValue(passwordField);
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
				new AsyncLogin(LoginActivity.this).execute(username, password, ip);
				//loginHttpRequest(username, password);
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
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
	 * Get the value of a edit text field
	 * @param field
	 * @return
	 */
	private String getEditTextValue(EditText field) {
		return field == null ? "" : field.getText().toString(); 
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
		if (!username.matches("[a-z0-9A-Z.]+")) {
			return false;
		} else return true;
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
