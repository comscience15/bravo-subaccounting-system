package com.bravo.bravomerchant.activities;


import com.bravo.bravomerchant.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * show some info
 * @author wenlong_jia
 *
 */
public class MessageActivity extends Activity {
	
	//the message will be showed
	private String msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		//get the message
        msg = getIntent().getStringExtra("msg");
        msg = msg == null || "".equals(msg) ? "Unknown Message." : msg;
	}

	@Override
    public void onStart() {
        super.onStart();

        //set the msg
        TextView msgTextView = (TextView) findViewById(R.id.msg);
        msgTextView.setText(msg);
        
        //set the button listener
        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {

				//turn to the home page
				Intent toMainPageIntent = new Intent(MessageActivity.this, MainActivity.class);
				startActivity(toMainPageIntent);
			}
        });
    }
}
