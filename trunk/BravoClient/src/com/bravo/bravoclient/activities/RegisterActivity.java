package com.bravo.bravoclient.activities;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.async.AsyncRegister;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This Class is taking charge of user registration
 * @author Daniel
 * @email danniel1205@gmail.com
 *
 */
public class RegisterActivity extends Activity {
	/** Declare buttons **/
	private static Button registerBtn;
	
	/** Declare registration fields **/
	private static EditText usernameField;
	private static EditText passwordField;
	private static EditText confirmPasswordField;
	private static EditText streetField;
	private static EditText cityField;
	private static Spinner stateField;
	private static EditText zipCodeField;

	/** Declaring strings used for registration **/
	private static String username;
	private static String password;
	private static String confirmPassword;
	private static String street;
	private static String city;
	private static String state;
	private static String zipCode;
	
	private boolean isValidUsername = false;
	private boolean isValidPassword = false;
	private boolean isValidConfirmP = false;
	private boolean isValidStreet = false;
	private boolean isValidCity = false;
	private boolean isValidZipCode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Setting the window for No title
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // Stop keyboard automatically generated
		setContentView(R.layout.activity_register);
		
		registerBtn = (Button) findViewById(R.id.register_Btn);
		registerBtn.setEnabled(false);
		
		usernameField = (EditText) findViewById(R.id.register_email);
		passwordField = (EditText) findViewById(R.id.register_password);
		confirmPasswordField = (EditText) findViewById(R.id.register_confirm_password);
		streetField = (EditText) findViewById(R.id.register_street);
		cityField = (EditText) findViewById(R.id.register_city);
		stateField = (Spinner) findViewById(R.id.register_state);
		zipCodeField = (EditText) findViewById(R.id.register_zip_code);
		
		setWatcher();
		
		registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println(username + "\n" + password + "\n" + confirmPassword + "\n" + street +"\n"+ state +"\n"+ zipCode +"\n");
				new AsyncRegister(getApplicationContext()).execute(username, password, street, city, state, zipCode, getString(R.string.IP_Address));
			}
		});
	}
	
	/**
	 *  This method is the watcher for all registration form fields
	 *  which will keep checking the fields during typing
	 */
	private void setWatcher() {
		/** username watcher **/
		TextWatcher usernameWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String usernameTemp = getEditTextValue(usernameField);
				if (usernameValidation(usernameTemp)) {
					isValidUsername = true;
					usernameField.setError(null);
					if (allFieldsValidated()) registerBtn.setEnabled(true);
					username = usernameTemp;
				} else {
					isValidUsername = false;
					registerBtn.setEnabled(false);
					usernameField.setError(getString(R.string.invalid_username));
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
			
		
		/** password watcher **/
		TextWatcher passwordWather = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String passwordTemp = getEditTextValue(passwordField);
				if (passwordValidation(passwordTemp)) {
					isValidPassword = true;
					passwordField.setError(null);
					if (allFieldsValidated()) registerBtn.setEnabled(true);
					password = passwordTemp;
				} else {
					isValidPassword = false;
					registerBtn.setEnabled(false);
					passwordField.setError(getString(R.string.reg_invalid_password));
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
		
		/** Confirm Password watcher **/
		TextWatcher confirmPasswordWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String confirmPasswordTemp = getEditTextValue(confirmPasswordField);
				if (passwordConfirm(password, confirmPasswordTemp)) {
					isValidConfirmP = true;
					confirmPasswordField.setError(null);
					if (allFieldsValidated()) registerBtn.setEnabled(true);
				} else {
					isValidConfirmP = false;
					registerBtn.setEnabled(false);
					confirmPasswordField.setError(getString(R.string.reg_password_not_match));
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		
		/** Street watcher**/
		TextWatcher streetWather = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				final String streetTemp = getEditTextValue(streetField);
				if (streetValidation(streetTemp)) {
					isValidStreet = true;
					streetField.setError(null);
					street = streetTemp;
					if (allFieldsValidated()) registerBtn.setEnabled(true);
				} else {
					isValidStreet = false;
					registerBtn.setEnabled(false);
					streetField.setError(getString(R.string.reg_invalid_street));
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			
		};
		
		/** City Watcher**/
		TextWatcher cityWather = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String cityTemp = getEditTextValue(cityField);
				if (cityValidation(cityTemp)) {
					isValidCity = true;
					cityField.setError(null);
					city = cityTemp;
					if (allFieldsValidated()) registerBtn.setEnabled(true);
				} else {
					isValidCity = false;
					registerBtn.setEnabled(false);
					cityField.setError(getString(R.string.reg_invalid_city));
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		
		/** Zipcode Watcher**/
		TextWatcher zipCodeWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				final String zipCodeTemp = getEditTextValue(zipCodeField);
				if (zipCodeValidation(zipCodeTemp)) {
					isValidZipCode = true;
					zipCodeField.setError(null);
					zipCode = zipCodeTemp;
					if (allFieldsValidated()) registerBtn.setEnabled(true);
				} else {
					isValidZipCode = false;
					registerBtn.setEnabled(false);
					zipCodeField.setError(getString(R.string.reg_invalid_zipCode));
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		
		/** **/
		state = getSpinnerValue(stateField);
		
		/** add listener to fields **/
		usernameField.addTextChangedListener(usernameWatcher);
		passwordField.addTextChangedListener(passwordWather);
		confirmPasswordField.addTextChangedListener(confirmPasswordWatcher);
		streetField.addTextChangedListener(streetWather);
		cityField.addTextChangedListener(cityWather);
		zipCodeField.addTextChangedListener(zipCodeWatcher);
	}
	
	/**
	 * The method is going to get the value of edit text field
	 * @param field
	 * @return
	 */
	private String getEditTextValue(EditText field) {
		return field == null ? "" : field.getText().toString(); 
	}
	
	/**
	 * The method is going to get the value of spinner field
	 * @param field
	 * @return
	 */
	private String getSpinnerValue(Spinner field) {
		return field == null ? "" : field.getSelectedItem().toString();
		
	}
	
	/**
	 * The username validation
	 * @param username
	 * @param password
	 * @return return true if username is following the rule
	 */
	private boolean usernameValidation(String username) {
		/** The username right now is not email address **/
		if (username.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}") ) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * The password validation
	 * @param password
	 * @return
	 */
	private boolean passwordValidation(String password) {
		if (password.matches("[a-z0-9A-Z]+") && (password.length() >=6)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * The password confirmation
	 * @param password
	 * @param confirmPassword
	 * @return
	 */
	private boolean passwordConfirm(String password, String confirmPassword) {
		return password.equals(confirmPassword) ? true : false;
	}
	
	/**
	 * Street validation
	 * @param street
	 * @return
	 */
	private boolean streetValidation(String street) {
		if (street != null && !street.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * City Validation
	 * @param city
	 * @return
	 */
	private boolean cityValidation(String city) {
		if (city != null && !city.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ZipCode Validation
	 * @param zipCode
	 * @return
	 */
	private boolean zipCodeValidation(String zipCode) {
		if (zipCode != null && zipCode.matches("[0-9]{5,6}")) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean allFieldsValidated() {
		return (isValidUsername && isValidPassword && isValidConfirmP && isValidStreet && isValidCity && isValidZipCode);
	}

}
