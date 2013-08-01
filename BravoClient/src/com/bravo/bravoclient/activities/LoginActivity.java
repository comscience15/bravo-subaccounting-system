package com.bravo.bravoclient.activities;

import com.bravo.bravoclient.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
		setContentView(R.layout.activity_login);
	}
	
	@Override
	public void onBackPressed() {
		Intent toLoginActivity = new Intent(LoginActivity.this.getApplicationContext(), MainActivity.class);
    	LoginActivity.this.startActivity(toLoginActivity);
    	LoginActivity.this.overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
	}
}
