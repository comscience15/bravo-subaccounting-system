package com.bravo.bravomerchant.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bravo.bravomerchant.activities.ScannerActivity;
import com.bravo.bravomerchant.async.AsyncPurchase;
import com.bravo.bravomerchant.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * Merchant main activity
 * @author wenlong_jia
 *
 */
public class MainActivity extends Activity {

	private boolean doublePressBackButton = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
    public void onStart() {
        super.onStart();

        /** Getting Scanner icon object on Home page*/
        final ImageView pay_scanner = (ImageView) findViewById(R.id.pay);
        
        /** Setting the onClick listener*/
        OnClickListener icon_listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.equals(pay_scanner)) {
					// Here we should triggle the barcode scanner library
					Intent intentForScannerActivity = new Intent(MainActivity.this, ScannerActivity.class);
					startActivity(intentForScannerActivity);
				}
			}
        };
        pay_scanner.setOnClickListener(icon_listener);
        
        /** get the logout button */
        final Button logout_button = (Button) findViewById(R.id.logout_button);
        /** when clicked,logout */
        logout_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showLoginActivity();
			}
		});
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	

    /**
     * Override the onBackPressed(), double click back button to exit the app
     */
    @Override
    public void onBackPressed() {
    	if (doublePressBackButton) {
    		Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    	}
    	this.doublePressBackButton = true;
    	
    	/** Setting the toast*/
    	Context context = getApplicationContext();
    	CharSequence text = getString(R.string.exit_app_toast);
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	
    	/** Setting boolean variable to false after 3sec*/
    	new Handler().postDelayed(new Runnable() {
    		@Override
    		public void run() {
    			doublePressBackButton = false;
    		}
    	}, 3000);
    }
    
	/**
	 * when logout, turn to loginActivity
	 */
    public void showLoginActivity() {
    	Intent toLoginActivity = new Intent(MainActivity.this.getApplicationContext(), LoginActivity.class);
    	MainActivity.this.startActivity(toLoginActivity);
    	MainActivity.this.overridePendingTransition(R.anim.login_enter, R.anim.login_out);
    	MainActivity.this.finish();
    }
    
}
