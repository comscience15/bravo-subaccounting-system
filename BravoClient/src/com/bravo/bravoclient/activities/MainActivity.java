package com.bravo.bravoclient.activities;
 
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.Toast;
 
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.bravo.bravoclient.R;
import com.bravo.bravoclient.adapters.PagerAdapter;
 
/**
 * This is the first activity after Splash screen
 * @author Daniel
 * @email danniel1205@gmail.com
 *
 */
public class MainActivity extends SherlockFragmentActivity {
    private ActionBar mActionBar;
    private ViewPager mPager;
    private Tab homeTab, cardsTab, rewardsTab;
    private Intent getIntentSource;
    private boolean doublePressBackButton;
    private static boolean ifLogin = false; // this is for future use, in order to remember if user has been logged in already
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // Stop keyboard automatically generated
               
        /** Getting a reference to action bar of this activity */
        mActionBar = getSupportActionBar();
       
        /** Disable the title bar*/
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        
        /** Setting the color of tabs**/
        mActionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
        
        
        /** Setting tab navigation mode */
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        //mActionBar.setBackgroundDrawable(getWallpaper());
        
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
 
        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
 
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            }
 
            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
            	/** If Cards tab is selected*/
            	if (tab.getPosition() == 1) {
            		/** Check if user has been login already**/
            		if(!ifLogin) showLoginActivity();
            	}
                mPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            }
        };
 
        /** Creating Home Tab */
        homeTab = mActionBar.newTab()
                .setText("Home")
                //.setIcon(R.drawable.android)
                .setTabListener(tabListener);
 
        mActionBar.addTab(homeTab);
 
        /** Creating Cards Tab */
        cardsTab = mActionBar.newTab()
                .setText("Cards")
                //.setIcon(R.drawable.apple)
                .setTabListener(tabListener);
        mActionBar.addTab(cardsTab);
        
        /** Creating Rewards Tab*/
        rewardsTab = mActionBar.newTab()
        		.setText("Rewards")
        		.setTabListener(tabListener);
        
        mActionBar.addTab(rewardsTab);

        /** Get the resource of intent **/
        getIntentSource = getIntent();
        String fromActivity = getIntentSource.getStringExtra("Activity");
        if(fromActivity != null && fromActivity.equals("Login")) {
        	ifLogin = true;
        	cardsTab.select();
        }
        
    }
    
    /** This method is going to create the menu inside of the title bar*/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = this.getSupportMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return true;
//    }
    
    
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
     * When Card tab has been selected, this will forward to LoginActivity
     */
    public void showLoginActivity() {
    	Intent toLoginActivity = new Intent(MainActivity.this.getApplicationContext(), LoginActivity.class);
    	MainActivity.this.startActivity(toLoginActivity);
    	MainActivity.this.overridePendingTransition(R.anim.login_enter, R.anim.login_out);
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
