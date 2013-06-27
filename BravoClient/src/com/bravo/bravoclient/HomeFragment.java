package com.bravo.bravoclient;
 
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
import com.actionbarsherlock.app.SherlockFragment;
 
public class HomeFragment extends SherlockFragment{
 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	/** Create the customized view for Home Tab*/
		View v = inflater.inflate(R.layout.activity_home, null);
        return v;
    }
 
    @SuppressLint("NewApi")
	@Override
    public void onStart() {
        super.onStart();
        /** Setting the background when view is starting */
        Drawable myDrawable = getResources().getDrawable(R.drawable.background);
        getView().setBackground(myDrawable);
    }
}