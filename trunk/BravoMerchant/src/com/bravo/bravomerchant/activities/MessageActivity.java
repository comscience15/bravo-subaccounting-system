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
import android.widget.TextView;
import android.widget.Toast;
/**
 * show some info
 * @author wenlong_jia
 *
 */
public class MessageActivity extends Activity {
	
	private String msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

        msg = getIntent().getStringExtra("msg");
	}

	@Override
    public void onStart() {
        super.onStart();

        TextView msgTextView = (TextView) findViewById(R.id.msg);
        msgTextView.setText(msg);
        
        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {

				Intent toMainPageIntent = new Intent(MessageActivity.this, MainActivity.class);
				startActivity(toMainPageIntent);
			}
        });
    }
}
