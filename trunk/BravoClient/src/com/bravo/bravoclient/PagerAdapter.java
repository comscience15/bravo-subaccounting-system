package com.bravo.bravoclient;
 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class PagerAdapter extends FragmentPagerAdapter{
 
    final int PAGE_COUNT = 3;
 
    /** Constructor of the class */
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){
            /** Home tab is selected */
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                data.putInt("current_page", arg0+1);
                homeFragment.setArguments(data);
                return homeFragment;
 
            /** Cards tab is selected */
            case 1:
                CardsFragment cardsFragment = new CardsFragment();
                data.putInt("current_page", arg0+1);
                cardsFragment.setArguments(data);
                return cardsFragment;
            
            /** Rewards tab is selected */
            case 2:
            	RewardsFragment rewardsFragment = new RewardsFragment();
            	data.putInt("current_page", arg0+1);
            	rewardsFragment.setArguments(data);
            	return rewardsFragment;
        }
        return null;
    }
 
    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}