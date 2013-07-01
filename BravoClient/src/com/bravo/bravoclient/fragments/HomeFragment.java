package com.bravo.bravoclient.fragments;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
 
import com.actionbarsherlock.app.SherlockFragment;
import com.bravo.bravoclient.R;
import com.bravo.bravoclient.R.drawable;
import com.bravo.bravoclient.R.id;
import com.bravo.bravoclient.R.layout;
import com.bravo.bravoclient.activities.ScannerActivity;
 
public class HomeFragment extends SherlockFragment{
 
	private Context cxt = null;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	/** Create the customized view for Home Tab*/
		View v = inflater.inflate(R.layout.activity_home, null);
		cxt = getActivity().getApplicationContext();
        return v;
    }
 
    @SuppressLint("NewApi")
	@Override
    public void onStart() {
        super.onStart();
        /** Setting the background when view is starting */
        Drawable myDrawable = getResources().getDrawable(R.drawable.background);
        getView().setBackground(myDrawable);
        
        /** Getting Scanner icon object on Home page*/
        final ImageView home_scanner = (ImageView) getView().findViewById(R.id.home_scanner);
        
        /** Setting the onClick listener*/
        OnClickListener icon_listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.equals(home_scanner)) {
					// Here we should triggle the barcode scanner library
					Intent intentForScannerActivity = new Intent(cxt, ScannerActivity.class);
					getActivity().startActivity(intentForScannerActivity);
				}
			}
        };
        home_scanner.setOnClickListener(icon_listener);
    }
}