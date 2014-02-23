package com.bravo.bravoclient.activities;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.R.layout;
import com.bravo.bravoclient.async.AsyncReloadMoney;
import com.bravo.bravoclient.common.CommonValidationHandler;
import com.bravo.bravoclient.common.CommonViewHandler;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;

public class ReloadMoneyActivity extends Activity {

	private static Logger logger = Logger.getLogger(ReloadMoneyActivity.class.getName());
	
	/** Declare form fields **/
	private static EditText firstnameField;
	private static EditText middleInitialField;
	private static EditText lastnameField;
	private static EditText cardNumberField;
	private static NumberPicker year;
	private static NumberPicker month;
	private static EditText cvnField;
	private static EditText streetField;
	private static EditText cityField;
	private static Spinner stateField;
	private static EditText zipCodeField;
	private static EditText amountField;
	private static CheckBox ifUseProfileCKB;
	private static CheckBox ifSaveProfileCKB;
	private static Button reloadBtnCredit;
	private static Button reloadBtnProfile;
	
	/** Declaring strings used for reload **/
	private static String firstName;
	private static String middleInitial;
	private static String lastName;
	private static String cardNumber;
	private static String expirationMonth;
	private static String expirationYear;
	private static String cvn;
	private static String street;
	private static String city;
	private static String state;
	private static String zipCode;
	private static String amount;
	private static String ip;
	
	private boolean isValidFirstname = false;
	private boolean isValidLastname = false;
	private boolean isValidCardNumber = false;
	private boolean isValidCVN = false;
	private boolean isValidStreet = false;
	private boolean isValidCity = false;
	private boolean isValidZipCode = false;
	private boolean isValidAmount = false;
	
	private static ArrayList<NameValuePair> paraList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Setting the window for No title
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // Stop keyboard automatically generated
		setContentView(R.layout.activity_reload_money);
		
		// Get filed view
		getFieldView();
		// Set picker for expiration date
		setNumberPicker();
		// Set profile checkbox
		setProfileCheckbox();
		// Set field wather
		setWatcher();
		
		setReloadBtnByCreditCardListener();
	}
	
	// Set the listener for credit card reload button
	// Note: This button is different from the button for profile reloading
	private void setReloadBtnByCreditCardListener() {
		reloadBtnCredit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setAPICallParameters();
				new AsyncReloadMoney(ReloadMoneyActivity.this).execute(getString(R.string.IP_Address), paraList);
				paraList = null; // Garbage collection
			}
		});
	}
	
	private void getFieldView() {
		firstnameField = (EditText) findViewById(R.id.reload_money_firstname);
		middleInitialField = (EditText) findViewById(R.id.reload_money_middle_initial);
		lastnameField = (EditText) findViewById(R.id.reload_money_lastname);
		cardNumberField = (EditText) findViewById(R.id.reload_money_card_number);
		year = (NumberPicker) findViewById(R.id.reload_money_expiration_year);
		month = (NumberPicker) findViewById(R.id.reload_money_expiration_month);
		cvnField = (EditText) findViewById(R.id.reload_money_cvn);
		streetField = (EditText) findViewById(R.id.reload_money_street);
		cityField = (EditText) findViewById(R.id.reload_money_city);
		stateField = (Spinner) findViewById(R.id.reload_money_state);
		zipCodeField = (EditText) findViewById(R.id.reload_money_zipcode);
		amountField = (EditText) findViewById(R.id.reload_money_amount);
		ifUseProfileCKB = (CheckBox) findViewById(R.id.use_profile_ckb);
		ifSaveProfileCKB = (CheckBox) findViewById(R.id.save_profile_ckb);
		reloadBtnCredit = (Button) findViewById(R.id.reload_money_Btn_credit_card);
		reloadBtnProfile = (Button) findViewById(R.id.reload_money_Btn_profile);
	}
	
	private void setAPICallParameters() {
		middleInitial = CommonViewHandler.getEditTextValue(middleInitialField);
		state = CommonViewHandler.getSpinnerValue(stateField);
		expirationMonth = CommonViewHandler.getNumebrPickerValue(month);
		expirationYear = CommonViewHandler.getNumebrPickerValue(year);
		
		String expirationDate = expirationMonth + expirationYear;
		String exitProfile = ifUseProfileCKB.isChecked() == true ? "true" : "false";
		String saveProfile = ifSaveProfileCKB.isChecked() == true ? "true" : "false";
		String cardID = getIntent().getStringExtra("cardID");

		paraList = new ArrayList<NameValuePair>();
		
		paraList.add(new BasicNameValuePair("firstName", firstName));
		paraList.add(new BasicNameValuePair("middleInitial", middleInitial));
		paraList.add(new BasicNameValuePair("lastName", lastName));
		paraList.add(new BasicNameValuePair("accountNumber", cardNumber));
		paraList.add(new BasicNameValuePair("cvn", cvn));
		paraList.add(new BasicNameValuePair("street", street));
		paraList.add(new BasicNameValuePair("city", city));
		paraList.add(new BasicNameValuePair("state", state));
		paraList.add(new BasicNameValuePair("zip", zipCode));
		paraList.add(new BasicNameValuePair("totalAmount", amount));
		paraList.add(new BasicNameValuePair("cardID", cardID));
		paraList.add(new BasicNameValuePair("expirationDate", expirationDate));
		paraList.add(new BasicNameValuePair("saveProfile", saveProfile));
		paraList.add(new BasicNameValuePair("existProfile", exitProfile));
		paraList.add(new BasicNameValuePair("paymentType", "C"));
		paraList.add(new BasicNameValuePair("creditCardProcessingType", "OP"));
		
		logger.info(paraList.toString());
	}
	
	private void setWatcher() {
		/** firstname watcher **/
		TextWatcher firstnameWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String firstnameTemp = CommonViewHandler.getEditTextValue(firstnameField);
				if (CommonValidationHandler.notNullValidation(firstnameTemp)) {
					isValidFirstname = true;
					firstnameField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					firstName = firstnameTemp;
				} else {
					isValidFirstname = false;
					reloadBtnCredit.setEnabled(false);
					firstnameField.setError(getString(R.string.reload_invalid_firstname));
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
		
		/** lastname watcher **/
		TextWatcher lastnameWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String lastnameTemp = CommonViewHandler.getEditTextValue(lastnameField);
				if (CommonValidationHandler.notNullValidation(lastnameTemp)) {
					isValidLastname = true;
					lastnameField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					lastName = lastnameTemp;
				} else {
					isValidLastname = false;
					reloadBtnCredit.setEnabled(false);
					lastnameField.setError(getString(R.string.reload_invalid_lastname));
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
		
		/** card number watcher **/
		TextWatcher cardNumberWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String cardNumberTemp = CommonViewHandler.getEditTextValue(cardNumberField);
				if (CommonValidationHandler.cardNumberValidation(cardNumberTemp)) {
					isValidCardNumber = true;
					cardNumberField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					cardNumber = cardNumberTemp;
				} else {
					isValidCardNumber = false;
					reloadBtnCredit.setEnabled(false);
					cardNumberField.setError(getString(R.string.reload_invalid_cardNumber));
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
		
		/** CVN watcher **/
		TextWatcher cvnWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String cvnTemp = CommonViewHandler.getEditTextValue(cvnField);
				if (CommonValidationHandler.cvnValidation(cvnTemp)) {
					isValidCVN = true;
					cvnField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					cvn = cvnTemp;
				} else {
					isValidCVN = false;
					reloadBtnCredit.setEnabled(false);
					cvnField.setError(getString(R.string.reload_invalid_cvn));
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
		
		/** street watcher **/
		TextWatcher streetWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String streetTemp = CommonViewHandler.getEditTextValue(streetField);
				if (CommonValidationHandler.notNullValidation(streetTemp)) {
					isValidStreet = true;
					streetField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					street = streetTemp;
				} else {
					isValidStreet = false;
					reloadBtnCredit.setEnabled(false);
					streetField.setError(getString(R.string.reload_invalid_street));
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
		
		/** city watcher **/
		TextWatcher cityWather = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String cityTemp = CommonViewHandler.getEditTextValue(cityField);
				if (CommonValidationHandler.notNullValidation(cityTemp)) {
					isValidCity = true;
					cityField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					city = cityTemp;
				} else {
					isValidCity = false;
					reloadBtnCredit.setEnabled(false);
					cityField.setError(getString(R.string.reload_invalid_city));
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
		
		/** zip code watcher **/
		TextWatcher zipCodeWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String zipCodeTemp = CommonViewHandler.getEditTextValue(zipCodeField);
				if (CommonValidationHandler.notNullValidation(zipCodeTemp)) {
					isValidZipCode = true;
					zipCodeField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					zipCode = zipCodeTemp;
				} else {
					isValidZipCode = false;
					reloadBtnCredit.setEnabled(false);
					zipCodeField.setError(getString(R.string.reload_invalid_zip_code));
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
		
		/** amount watcher **/
		TextWatcher amountWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				final String amountTemp = CommonViewHandler.getEditTextValue(amountField);
				if (CommonValidationHandler.amountValidation(amountTemp)) {
					isValidAmount = true;
					amountField.setError(null);
					if (allFieldsValidated()) reloadBtnCredit.setEnabled(true);
					amount = amountTemp;
				} else {
					isValidAmount = false;
					reloadBtnCredit.setEnabled(false);
					amountField.setError(getString(R.string.reload_invalid_amount));
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
		
		
		
		// Add listener to fields
		firstnameField.addTextChangedListener(firstnameWatcher);
		lastnameField.addTextChangedListener(lastnameWatcher);
		cardNumberField.addTextChangedListener(cardNumberWatcher);
		cvnField.addTextChangedListener(cvnWatcher);
		streetField.addTextChangedListener(streetWatcher);
		cityField.addTextChangedListener(cityWather);
		zipCodeField.addTextChangedListener(zipCodeWatcher);
		amountField.addTextChangedListener(amountWatcher);
		
	}
	
	/**
	 * Set the profile checkbox
	 */
	private void setProfileCheckbox() {
		// Add logic actions for check box if going to use profile for payment
				CheckBox ifUseProfile = (CheckBox) findViewById(R.id.use_profile_ckb);
				ifUseProfile.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Spinner profileSpinner = (Spinner) findViewById(R.id.reload_money_exist_profile);
						Button profileSubmitButton = (Button) findViewById(R.id.reload_money_Btn_profile);
						ScrollView reloadInfoForm = (ScrollView) findViewById(R.id.reload_money_form_scrollview);
						if (isChecked) {
							profileSpinner.setVisibility(View.VISIBLE);
							profileSubmitButton.setVisibility(View.VISIBLE);
							reloadInfoForm.setVisibility(View.GONE);
						} else {
							profileSpinner.setVisibility(View.GONE);
							profileSubmitButton.setVisibility(View.GONE);
							reloadInfoForm.setVisibility(View.VISIBLE);
						}
					}
				});
	}
	
	/**
	 * Set the number picker for expiration month and year
	 */
	private void setNumberPicker() {
		final String[] months = getResources().getStringArray(R.array.reload_money_expiration_month);
		final String[] years = getResources().getStringArray(R.array.reload_money_expiration_year);
		month.setMinValue(1);
		month.setMaxValue(12);
		month.setWrapSelectorWheel(false);
		year.setMinValue(14);
		year.setMaxValue(20);
		year.setWrapSelectorWheel(false);
		month.setDisplayedValues(months);
		year.setDisplayedValues(years);
	}
	
	private boolean allFieldsValidated() {
		return (isValidFirstname && isValidLastname && isValidCardNumber && isValidCVN && isValidStreet && isValidCity && isValidZipCode && isValidAmount);
	}
}
