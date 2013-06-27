package com.bravo.bravoclient;
 
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
 
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
 
public class HomeFragment extends SherlockFragment{
 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.activity_home, null);
//        /** Creating array adapter to set data in listview */
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.activity_main, android_versions);
// 
//        /** Setting the array adapter to the listview */
//        setListAdapter(adapter);
    	
    	
 
        return v;//super.onCreateView(inflater, container, savedInstanceState);
    }
 
    @SuppressLint("NewApi")
	@Override
    public void onStart() {
        super.onStart();
        /** Setting the multiselect choice mode for the listview */
        Drawable myDrawable = getResources().getDrawable(R.drawable.background);
        getView().setBackground(myDrawable);
        //getView().set.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}