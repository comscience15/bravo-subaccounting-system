package com.bravo.bravoclient.activities;

import com.bravo.bravoclient.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * This Class is taking charge of user registration
 * @author Daniel
 * @email danniel1205@gmail.com
 *
 */
public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // No title
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // Stop keyboard automatically generated
		setContentView(R.layout.activity_register);
	}


}
