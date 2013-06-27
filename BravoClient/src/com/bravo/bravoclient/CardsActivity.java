package com.bravo.bravoclient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class CardsActivity extends SherlockActivity implements ActionBar.TabListener {

	private ActionBar.Tab tab1, tab2, tab3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTheme(R.style.AppTheme); //Used for theme switching in samples
				super.onCreate(savedInstanceState);

				setContentView(R.layout.activity_cards);

				getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				// Used for setting action bar theme
				getSupportActionBar().setBackgroundDrawable(getWallpaper()); 
				
				// Define the Tab
				tab1 = getSupportActionBar().newTab();
				tab1.setIcon(R.drawable.nv_home);
				tab1.setText("Home");
				tab1.setTabListener(this);		

				// Define the Tab
				tab2 = getSupportActionBar().newTab();
				tab2.setIcon(R.drawable.nv_cards);
				tab2.setText("Cards");
				tab2.setTabListener(this);

				// Define the Tab
				tab3 = getSupportActionBar().newTab();
				tab3.setIcon(R.drawable.nv_rewards);
				tab3.setText("Rewards");
				tab3.setTabListener(this);
				
				// Define the Tab
//				ActionBar.Tab tab4 = getSupportActionBar().newTab();
//				tab4.setIcon(R.drawable.nv_search);
//				//tab4.setText("Rewards");
//				tab4.setTabListener(this);
//				getSupportActionBar().addTab(tab4);
				
				// add the tab2 first, since it will be selected at the beginning
				getSupportActionBar().addTab(tab1, false);
				getSupportActionBar().addTab(tab2, false);
				getSupportActionBar().addTab(tab3, false);
				
				getSupportActionBar().selectTab(tab2);
				
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		if (tab.equals(tab1)) {
			System.out.println("Cards: Tab 1 is selected");
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
		}
		if (tab.equals(tab3)) {
			System.out.println("Cards: Tab 3 is selected");
		}
		
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.cards, menu);
//		return true;
//	}

}
