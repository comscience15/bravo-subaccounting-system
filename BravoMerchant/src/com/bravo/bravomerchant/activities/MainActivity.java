package com.bravo.bravomerchant.activities;


import com.bravo.bravomerchant.activities.ScannerActivity;
import com.bravo.bravomerchant.async.AsyncLogout;
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
    private static boolean isLogin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent getIntentSource = getIntent();
        String fromActivity = getIntentSource.getStringExtra("Activity");
        // enable the page jumping from login and register directly to cards fragment
        if(fromActivity != null && (fromActivity.equals("Login") || fromActivity.equals("Register"))) {
        	
        	isLogin = true;
        }else{

	    	Intent toLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
	    	MainActivity.this.startActivity(toLoginActivity);
        }
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
					
					if(isLogin){
						// Here we should triggle the barcode scanner library
						Intent intentForScannerActivity = new Intent(MainActivity.this, ScannerActivity.class);
						startActivity(intentForScannerActivity);
					}else{
				    	Intent toLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
				    	MainActivity.this.startActivity(toLoginActivity);
					}
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
				logout();
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
    public void logout() {

    	new AsyncLogout(MainActivity.this).execute(getString(R.string.IP_Address));
//    	Intent toLoginActivity = new Intent(MainActivity.this.getApplicationContext(), LoginActivity.class);
//    	MainActivity.this.startActivity(toLoginActivity);
//    	MainActivity.this.overridePendingTransition(R.anim.login_enter, R.anim.login_out);
//    	MainActivity.this.finish();
    }

	public static boolean isLogin() {
		return isLogin;
	}

	public static void setLogin(boolean isLogin) {
		MainActivity.isLogin = isLogin;
	}
    
}
