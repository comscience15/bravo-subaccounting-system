package com.bravo.bravoclient.activities;
 
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
 
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.bravo.bravoclient.R;
import com.bravo.bravoclient.adapters.PagerAdapter;
 
public class MainActivity extends SherlockFragmentActivity {
    ActionBar mActionBar;
    ViewPager mPager;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        /** Getting a reference to action bar of this activity */
        mActionBar = getSupportActionBar();
 
        /** Set tab navigation mode */
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
       
        mActionBar.setBackgroundDrawable(getWallpaper());
        
        /** Getting a reference to ViewPager from the layout */
        mPager = (ViewPager) findViewById(R.id.pager);
 
        /** Getting a reference to FragmentManager */
        FragmentManager fm = getSupportFragmentManager();
 
        /** Defining a listener for pageChange */
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionBar.setSelectedNavigationItem(position);
            }
        };
 
        /** Setting the pageChange listner to the viewPager */
        mPager.setOnPageChangeListener(pageChangeListener);
 
        /** Creating an instance of FragmentPagerAdapter */
        PagerAdapter fragmentPagerAdapter = new PagerAdapter(fm);
 
        /** Setting the FragmentPagerAdapter object to the viewPager object */
        mPager.setAdapter(fragmentPagerAdapter);
 
        mActionBar.setDisplayShowTitleEnabled(true);
 
        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
 
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            }
 
            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            }
        };
 
        /** Creating Home Tab */
        Tab tab = mActionBar.newTab()
                .setText("Home")
                //.setIcon(R.drawable.android)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
 
        /** Creating Cards Tab */
        tab = mActionBar.newTab()
                .setText("Cards")
                //.setIcon(R.drawable.apple)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
        
        /** Creating Rewards Tab*/
        tab = mActionBar.newTab()
        		.setText("Rewards")
        		.setTabListener(tabListener);
        
        mActionBar.addTab(tab);
 
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
 
}






















//package com.bravo.bravoclient;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
//
//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.ActionBar.Tab;
//import com.actionbarsherlock.app.SherlockActivity;
//
//import com.bravo.bravoclient.R;
//
//public class MainActivity extends SherlockActivity implements
//		ActionBar.TabListener {
//	private ActionBar.Tab tab1, tab2, tab3;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// setTheme(R.style.AppTheme); //Used for theme switching in samples
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.activity_main);
//		
//		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		// Used for setting action bar theme
//		getSupportActionBar().setBackgroundDrawable(getWallpaper()); 
//		
//		// Define the Tab
//		tab1 = getSupportActionBar().newTab();
//		tab1.setIcon(R.drawable.nv_home);
//		tab1.setText("Home");
//		tab1.setTabListener(this);		
//		
//		// Define the Tab
//		tab2 = getSupportActionBar().newTab();
//		tab2.setIcon(R.drawable.nv_cards);
//		tab2.setText("Cards");
//		tab2.setTabListener(this);
//
//		// Define the Tab
//		tab3 = getSupportActionBar().newTab();
//		tab3.setIcon(R.drawable.nv_rewards);
//		tab3.setText("Rewards");
//		tab3.setTabListener(this);
//		
//		// Define the Tab
////		ActionBar.Tab tab4 = getSupportActionBar().newTab();
////		tab4.setIcon(R.drawable.nv_search);
////		//tab4.setText("Rewards");
////		tab4.setTabListener(this);
////		getSupportActionBar().addTab(tab4);
//		
//		getSupportActionBar().addTab(tab1, false);
//		getSupportActionBar().addTab(tab2, false);
//		getSupportActionBar().addTab(tab3, false);
//		
//		// Home tab is selected by default in Home view
//		getSupportActionBar().selectTab(tab1);
//
//	}
//
//	@Override
//	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
//	}
//
//	@Override
//	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
//		if (tab.equals(tab2)) {
//			System.out.println("MainActivity: Tab 2 is selected");
//			Intent intent = new Intent(this, CardsActivity.class);
//			startActivity(intent);
//			overridePendingTransition(0, 0);
//		}
//	}
//
//	@Override
//	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
//	}
//}
