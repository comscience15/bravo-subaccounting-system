package com.bravo.bravoclient.activities;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.async.AsyncLogin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
/**
 * This class is for log in activity, and login info validation
 * The network request part is in AsyncLogin.java
 * @author Daniel
 *
 */
public class LoginActivity extends Activity {
	/** Declaring buttons**/
	private Button loginBtn;
	private Button registerBtn;
	
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
		setContentView(R.layout.activity_login);
		
		ip = getString(R.string.IP_Address);
		
		/** Declaring buttons**/
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginBtn.setEnabled(false);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		
		/** Declaring text fields**/
		usernameField = (EditText) findViewById(R.id.email);
		passwordField = (EditText) findViewById(R.id.password);
		
		/** Declaring the username watcher**/
		TextWatcher usernameWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String usernameTemp = getUsername(usernameField);
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
				final String passwordTemp = getPassword(passwordField);
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
				new AsyncLogin(LoginActivity.this).execute(username, password, ip);
				//loginHttpRequest(username, password);
			}
		});
		
		/** Setting up the click listener for Register button**/
		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		Intent toLoginActivity = new Intent(LoginActivity.this.getApplicationContext(), MainActivity.class);
    	LoginActivity.this.startActivity(toLoginActivity);
    	LoginActivity.this.overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
	}
	
	private String getUsername(EditText usernameView) {
		return usernameView == null ? "" : usernameView.getText().toString(); 
	}
	
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
